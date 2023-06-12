package com.example.pizzeria_aplikacja

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.content.DialogInterface
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Filter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import java.sql.CallableStatement
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CustomAdapter(context: Context, private val itemList: MutableList<Item>) :
    ArrayAdapter<Item>(context, 0, itemList) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var originalItemList: MutableList<Item> = ArrayList(itemList)
    private var filteredItemList: MutableList<Item> = ArrayList(itemList)
    private var isAddingItem: Boolean = false
    private val enteredTextMap: MutableMap<Int, String> = mutableMapOf()
    var ID_TRANSAKCJI: Int = 0
    var ID_STOLIKA: Int = 0
    var ID_KLIENTA: Int = 0
    var DATA_ROZPOCZECIA_TRANSAKCJI: String = ""
    var DATA_ZAKONCZENIA_TRANSAKCJI: String = ""
    var SPOSOB_PLATNOSCI: String = ""
    var KWOTA: Int = 0
    //var ID_DANIA: Int = 0
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    init {
        wczytajtransakcje(context)
    }

    override fun getCount(): Int {
        return filteredItemList.size
    }

    override fun getItem(position: Int): Item {
        return itemList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            val inflatedView = inflater.inflate(R.layout.list_item_layout, parent, false)
            itemView = inflatedView
        }

        val textView1: EditText? = itemView?.findViewById(R.id.text_view1)
        val textView2: EditText? = itemView?.findViewById(R.id.text_view2)
        val textView3: EditText? = itemView?.findViewById(R.id.text_view3)
        val iconButton: ImageButton? = itemView?.findViewById(R.id.icon_button)
        val iconButton2: ImageButton? = itemView?.findViewById(R.id.icon_button_2)

        val item: Item = getItem(position)
        item?.let {
            textView1?.setText(item.id_transakcji)
            textView2?.setText(item.dataRozpoczecia)
            textView3?.setText(item.kwota)
            textView1?.isEnabled = false
            textView2?.isEnabled = false
            textView3?.isEnabled = false

            iconButton?.setImageResource(item.iconResId)
            iconButton2?.setImageResource(item.deleteIconResId)

            iconButton2?.setOnClickListener {
                if (!item.isEmptyItem) {
                    val alertDialogBuilder = AlertDialog.Builder(context)
                    alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć?")
                    alertDialogBuilder.setPositiveButton("Usuń") { dialog, which ->
                        item.let {
                            try {
                                var query = "call usun_transakcje(\'${item.id_transakcji}\')"
                                val callableStatement: CallableStatement =
                                    connection?.prepareCall(query)!!
                                callableStatement.execute()

                                connection?.commit()
                                callableStatement.close()
                            } catch (e: SQLException) {
                                e.printStackTrace()
                                Toast.makeText(
                                    context,
                                    "Operacja usunięcia nie powiodła się",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            itemList.remove(it)
                            originalItemList.remove(it)
                            filteredItemList.remove(it)
                            notifyDataSetChanged()
                            Toast.makeText(context, "Element został usunięty", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    alertDialogBuilder.setNegativeButton("Anuluj") { dialog, which ->
                        // Anuluj usunięcie
                    }
                    alertDialogBuilder.show()
                }
            }
            iconButton?.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Szczegółowe informacje")

                val inputLayout = View.inflate(context, R.layout.informacje, null)
                builder.setView(inputLayout)

                val param1TextView = inputLayout.findViewById<TextView>(R.id.param1TextView)
                val param2TextView = inputLayout.findViewById<TextView>(R.id.param2TextView)
                val param3TextView = inputLayout.findViewById<TextView>(R.id.param3TextView)
                val param4TextView = inputLayout.findViewById<TextView>(R.id.param4TextView)
                val param5TextView = inputLayout.findViewById<TextView>(R.id.param5TextView)
                val param6TextView = inputLayout.findViewById<TextView>(R.id.param6TextView)
                param1TextView.setText("Id stolika: " + item.id_stolika)
                param2TextView.setText("Id klienta: " + item.id_klienta)
                param3TextView.setText("Data rozpoczęcia: " + item.dataRozpoczecia)
                param4TextView.setText("Data zakonczenia: " + item.dataZakonczenia)
                param5TextView.setText("Sposób płatnośći: " + item.sposobPlatnosci)
                param6TextView.setText("Kwota: " + item.kwota)

                builder.setPositiveButton("OK") { _, _ ->
                }
                builder.show()
            }
        }
        return itemView!!
    }

    fun showParameterInputDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Podaj informacje o transakcji")

        val inputLayout = View.inflate(context, R.layout.dialog_input, null)
        builder.setView(inputLayout)

        val param1EditText = inputLayout.findViewById<EditText>(R.id.param1EditText)
        val param2EditText = inputLayout.findViewById<EditText>(R.id.param2EditText)
        val param3DatePicker = inputLayout.findViewById<DatePicker>(R.id.param3DatePicker)
        val param4DatePicker = inputLayout.findViewById<DatePicker>(R.id.param4DatePicker)
        val param5EditText = inputLayout.findViewById<EditText>(R.id.param5EditText)
        val param6EditText = inputLayout.findViewById<EditText>(R.id.param6EditText)
        val param7EditText = inputLayout.findViewById<EditText>(R.id.param7EditText)
        val paramtimePicker1 = inputLayout.findViewById<TimePicker>(R.id.timePicker1)
        val paramtimePicker2 = inputLayout.findViewById<TimePicker>(R.id.timePicker2)
        //val param7EditText = inputLayout.findViewById<EditText>(R.id.param7EditText)
        param7EditText.visibility = View.GONE

        //TODO dodać listę dań
        param1EditText.setHint("Podaj id stolika")
        param2EditText.setHint("Podaj id klienta")
        param5EditText.setHint("Podaj sposób płatności")
        param6EditText.setHint("Podaj kwotę")

        var calendar: Calendar
        var year: Int
        var month: Int
        var day: Int
        var hourOfDay: Int
        var minute: Int
        var time: String

        builder.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            ID_STOLIKA = try {
                param1EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }
            ID_KLIENTA = try {
                param2EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }
            year = param3DatePicker.year
            month = param3DatePicker.month
            day = param3DatePicker.dayOfMonth
            calendar = Calendar.getInstance()
            hourOfDay = paramtimePicker1.hour
            minute = paramtimePicker1.minute
            calendar.set(year, month, day)
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            time = dateFormat.format(calendar.time)

            DATA_ROZPOCZECIA_TRANSAKCJI = try {
                time
            } catch (e: Exception) {
                ""
            }
            year = param4DatePicker.year
            month = param4DatePicker.month
            day = param4DatePicker.dayOfMonth
            hourOfDay = paramtimePicker2.hour
            minute = paramtimePicker2.minute

            calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            time = dateFormat.format(calendar.time)
            DATA_ZAKONCZENIA_TRANSAKCJI = try {
                time
            } catch (e: Exception) {
                ""
            }
            SPOSOB_PLATNOSCI = param5EditText.text.toString()

            KWOTA = try {
                param6EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }
//            ID_DANIA = try {
//                param7EditText.text.toString().toInt()
//            } catch (e: Exception) {
//                0
//            }
            addItem()
        }
        builder.setNegativeButton("Anuluj", null)

        val dialog = builder.create()
        dialog.show()
    }

    fun addItem() {
        try {
            val dataRoz = DATA_ROZPOCZECIA_TRANSAKCJI//"2023-06-19 15:00:00"
            val dataZak = DATA_ZAKONCZENIA_TRANSAKCJI
            val query =
                "{call dodaj_transakcje(?, ?, TO_DATE('$dataRoz', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('$dataZak', 'YYYY-MM-DD HH24:MI:SS'), ?, ?, SYS.ODCINUMBERLIST(1))}"

            val callableStatement: CallableStatement = connection?.prepareCall(query)!!
            callableStatement.setInt(1, ID_STOLIKA)
            callableStatement.setInt(2, ID_KLIENTA)
            callableStatement.setString(3, SPOSOB_PLATNOSCI)
            callableStatement.setInt(4, KWOTA)
            callableStatement.execute()
            wczytajtransakcje(context)

            notifyDataSetChanged()
            connection?.commit()
            callableStatement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            Toast.makeText(
                context,
                "Błędne dane! Operacja nie powiodła się",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Item>()

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(originalItemList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in originalItemList) {
                        if (item.id_transakcji.toLowerCase().contains(filterPattern) ||
                            item.dataRozpoczecia.toLowerCase().contains(filterPattern)
                        ) {
                            filteredList.add(item)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItemList.clear()
                if (results != null && results.values is List<*>) {
                    val filteredList = results.values as List<Item>
                    filteredItemList.addAll(filteredList)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun wczytajtransakcje(context: Context) {
        try {
            itemList.clear()
            originalItemList.clear()
            filteredItemList.clear()
            val statement = connection?.createStatement()
            val query = "SELECT * FROM TRANSAKCJA"
            val resultSet = statement?.executeQuery(query)
            lateinit var item: Item
            lateinit var item1: Item
            if (resultSet != null) {
                while (resultSet.next()) {
                    ID_TRANSAKCJI = resultSet.getInt("ID_TRANSAKCJI")
                    ID_STOLIKA = resultSet.getInt("ID_STOLIKA")
                    ID_KLIENTA = resultSet.getInt("ID_KLIENTA")
                    DATA_ROZPOCZECIA_TRANSAKCJI = resultSet.getDate("DATA_ROZPOCZECIA_TRANSAKCJI").toString()
                    DATA_ROZPOCZECIA_TRANSAKCJI += " " + resultSet.getTime("DATA_ROZPOCZECIA_TRANSAKCJI").toString()
                    DATA_ZAKONCZENIA_TRANSAKCJI = resultSet.getDate("DATA_ZAKONCZENIA_TRANSAKCJI").toString()
                    DATA_ZAKONCZENIA_TRANSAKCJI += " " + resultSet.getTime("DATA_ZAKONCZENIA_TRANSAKCJI").toString()
                    SPOSOB_PLATNOSCI = resultSet.getString("SPOSOB_PLATNOSCI")
                    KWOTA = resultSet.getInt("KWOTA")
                    //val idDania = resultSet.getInt("ID_DANIA")
                    item = Item(
                        ID_TRANSAKCJI.toString(),
                        ID_STOLIKA.toString(),
                        ID_KLIENTA.toString(),
                        DATA_ROZPOCZECIA_TRANSAKCJI,
                        DATA_ZAKONCZENIA_TRANSAKCJI,
                        SPOSOB_PLATNOSCI,
                        KWOTA.toString(),
                        R.drawable.ic_pytajnik_foreground,
                        R.drawable.ic_kosz_foreground,
                        R.drawable.ic_kosz_foreground,
                        false
                    )

                    itemList.add(item)
                    originalItemList.add(item)
                    filteredItemList.add(item)
                }
            }
            connection?.commit()
            statement?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            Toast.makeText(
                context,
                "Operacja wczytania nie powiodła się",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
