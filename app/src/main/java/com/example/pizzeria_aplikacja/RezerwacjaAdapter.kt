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

class RezerwacjaAdapter(
    context: Context,
    private val itemListRezerwacja: MutableList<ItemRezerwacja>
) :
    ArrayAdapter<ItemRezerwacja>(context, 0, itemListRezerwacja) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var originalItemList: MutableList<ItemRezerwacja> = ArrayList(itemListRezerwacja)
    private var filteredItemList: MutableList<ItemRezerwacja> = ArrayList(itemListRezerwacja)
    private var isAddingItem: Boolean = false
    private val enteredTextMap: MutableMap<Int, String> = mutableMapOf()
    var ID_REZERWACJI: Int = 0
    var ID_KLIENTA: Int = 0
    var LICZBA_OSOB: Int = 0
    var DATA: String = ""
    var GODZINA: String = ""
    var LICZBA_GODZIN: Int = 0
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val hourAndDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val hourFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    lateinit var param1EditText: EditText
    lateinit var param2EditText: EditText
    lateinit var param3DatePicker: DatePicker
    lateinit var param4DatePicker: DatePicker
    lateinit var param5EditText: EditText
    lateinit var param6EditText: EditText
    lateinit var param7EditText: EditText
    lateinit var paramtimePicker1: TimePicker
    lateinit var paramtimePicker2: TimePicker


    init {
        wczytajtransakcje(context)
    }

    override fun getCount(): Int {
        return filteredItemList.size
    }

    override fun getItem(position: Int): ItemRezerwacja? {
        return filteredItemList[position]
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

        val item: ItemRezerwacja? = getItem(position)
        item?.let {
            textView1?.setText(item.id_rezerwacji)
            textView2?.setText(item.data)
            textView3?.setText(item.liczbaOsob)
            textView1?.isEnabled = false
            textView2?.isEnabled = false
            textView3?.isEnabled = false

            item.iconResId = R.drawable.ic_pytajnik_foreground
            item.deleteIconResId = R.drawable.ic_pen_foreground
            iconButton?.setImageResource(item.iconResId)
            iconButton2?.setImageResource(item.deleteIconResId)

            iconButton2?.setOnClickListener {
                item.iconResId = R.drawable.ic_pen_foreground
                item.deleteIconResId = R.drawable.ic_kosz_foreground
                iconButton?.setImageResource(item.iconResId)
                iconButton2.setImageResource(item.deleteIconResId)
                if (item.isEditing) {
                    Usun(item)
                    item.isEditing = false
                    item.iconResId = R.drawable.ic_pytajnik_foreground
                    item.deleteIconResId = R.drawable.ic_pen_foreground
                    iconButton?.setImageResource(item.iconResId)
                    iconButton2.setImageResource(item.deleteIconResId)
                } else item.isEditing = true
            }

            iconButton?.setOnClickListener {
                if (!item.isEditing) {
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

                    param1TextView.setText("Id klienta: " + item.id_klienta.toString())
                    param2TextView.setText("Liczba osób: " + item.liczbaOsob.toString())
                    param3TextView.setText("Data: " + item.data.toString())
                    param4TextView.setText("Godzina: " + item.godzina.toString())
                    param5TextView.setText("Liczba godzin: " + item.liczbaGodzin)
                    param6TextView.visibility = View.GONE

                    builder.setPositiveButton("OK") { _, _ ->
                    }
                    builder.show()
                } else {
                    showParameterInputDialog(item)
                    item.iconResId = R.drawable.ic_pytajnik_foreground
                    item.deleteIconResId = R.drawable.ic_pen_foreground
                    iconButton.setImageResource(item.iconResId)
                    iconButton2?.setImageResource(item.deleteIconResId)
                }
            }
        }
        return itemView!!
    }

    fun showParameterInputDialog(item: ItemRezerwacja?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Podaj informacje o rezerwacji")

        val inputLayout = View.inflate(context, R.layout.dialog_input, null)
        builder.setView(inputLayout)

        param1EditText = inputLayout.findViewById<EditText>(R.id.param1EditText)
        param2EditText = inputLayout.findViewById<EditText>(R.id.param2EditText)
        param3DatePicker = inputLayout.findViewById<DatePicker>(R.id.param3DatePicker)
        param4DatePicker = inputLayout.findViewById<DatePicker>(R.id.param4DatePicker)
        param5EditText = inputLayout.findViewById<EditText>(R.id.param5EditText)
        param6EditText = inputLayout.findViewById<EditText>(R.id.param6EditText)
        param7EditText = inputLayout.findViewById<EditText>(R.id.param7EditText)
        paramtimePicker1 = inputLayout.findViewById<TimePicker>(R.id.timePicker1)
        paramtimePicker2 = inputLayout.findViewById<TimePicker>(R.id.timePicker2)

        param1EditText.setHint("Podaj id klienta")
        param2EditText.setHint("Podaj liczbę osób")
        param5EditText.setHint("Podaj liczbę godzin")
        param6EditText.visibility = View.GONE
        param4DatePicker.visibility = View.GONE
        param7EditText.visibility = View.GONE
        paramtimePicker2.visibility = View.GONE

        var calendar: Calendar
        var dataFormedData: Date
        var hourFormattedHour: Date
        var year: Int
        var month: Int
        var day: Int
        var hourOfDay: Int
        var minute: Int

        if (item != null) {
            param1EditText.setText(item.id_klienta)
            param2EditText.setText(item.liczbaOsob)
            calendar = Calendar.getInstance()
            dataFormedData = dateFormat.parse(item.data)
            calendar.time = dataFormedData
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            param3DatePicker.init(year, month, day, null)

            calendar = Calendar.getInstance()
            hourFormattedHour = hourFormat.parse(item.godzina)
            calendar.time = hourFormattedHour
            hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
            paramtimePicker1.hour = hourOfDay
            paramtimePicker1.minute = minute

            param5EditText.setText(item.liczbaGodzin)
        }

        builder.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            ID_KLIENTA = try {
                param1EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }
            LICZBA_OSOB = try {
                param2EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }
            year = param3DatePicker.year
            month = param3DatePicker.month
            day = param3DatePicker.dayOfMonth
            calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            //var sqlDate = java.sql.Date(calendar.time.time)
            val date = dateFormat.format(calendar.time)
            DATA = try {
                date
            } catch (e: Exception) {
                ""
            }
            calendar = Calendar.getInstance()
             hourOfDay = paramtimePicker1.hour
             minute = paramtimePicker1.minute
            calendar.set(year, month, day)
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            val time = hourAndDateFormat.format(calendar.time)

            GODZINA = try {
                time
            } catch (e: Exception) {
                ""
            }
            LICZBA_GODZIN = try {
                param5EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }
            if (item != null) {
                update(item)
            } else{
                addItem()
            }
        }
        builder.setNegativeButton("Anuluj", null)

        item?.isEditing = false
        val dialog = builder.create()
        dialog.show()
    }

    fun addItem() {
        try {
            val data = DATA
            val godzina = GODZINA
            val query =
                "{call dodaj_rezerwacje(?, ?, TO_DATE('$data', 'YYYY-MM-DD'), TO_DATE('$godzina', 'YYYY-MM-DD HH24:MI:SS'), ?, SYS.ODCINUMBERLIST(1))}"
            val callableStatement: CallableStatement = connection?.prepareCall(query)!!
            callableStatement.setInt(1, ID_KLIENTA)
            callableStatement.setInt(2, LICZBA_OSOB)
            callableStatement.setInt(3, LICZBA_GODZIN)
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

    fun update(item: ItemRezerwacja) {
        try {
            val data = DATA
            val godzina = GODZINA
            val query =
                "UPDATE REZERWACJA SET ID_KLIENTA = ?, LICZBA_OSOB = ?, DATA = TO_DATE('$data', 'YYYY-MM-DD'), GODZINA = TO_DATE('$godzina', 'YYYY-MM-DD HH24:MI:SS'), LICZBA_GODZIN = ? WHERE ID_REZERWACJI = ${item.id_rezerwacji}"
            val statement = connection?.prepareStatement(query)
            statement?.setInt(1, ID_KLIENTA)
            statement?.setInt(2, LICZBA_OSOB)
            statement?.setInt(3, LICZBA_GODZIN)
            statement?.executeUpdate()
            wczytajtransakcje(context)
            notifyDataSetChanged()

            connection?.commit()
            statement?.close()
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
                val filteredList = mutableListOf<ItemRezerwacja>()

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(originalItemList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in originalItemList) {
                        if (item.id_rezerwacji.toLowerCase().contains(filterPattern) ||
                            item.data.toLowerCase().contains(filterPattern)
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
                    val filteredList = results.values as List<ItemRezerwacja>
                    filteredItemList.addAll(filteredList)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun wczytajtransakcje(context: Context) {
        try {
            itemListRezerwacja.clear()
            originalItemList.clear()
            filteredItemList.clear()
            val statement = connection?.createStatement()
            val query = "SELECT * FROM REZERWACJA"
            val resultSet = statement?.executeQuery(query)
            lateinit var item: ItemRezerwacja
            if (resultSet != null) {
                while (resultSet.next()) {
                    ID_REZERWACJI = resultSet.getInt("ID_REZERWACJI")
                    ID_KLIENTA = resultSet.getInt("ID_KLIENTA")
                    LICZBA_OSOB = resultSet.getInt("LICZBA_OSOB")
                    DATA = resultSet.getDate("DATA").toString()
                    GODZINA = resultSet.getTime("GODZINA").toString()
                    LICZBA_GODZIN = resultSet.getInt("LICZBA_GODZIN")
                    item = ItemRezerwacja(
                        ID_REZERWACJI.toString(),
                        ID_KLIENTA.toString(),
                        LICZBA_OSOB.toString(),
                        DATA,
                        GODZINA,
                        LICZBA_GODZIN.toString(),

                        R.drawable.ic_pytajnik_foreground,
                        R.drawable.ic_pen_foreground,
                        false
                    )
                    itemListRezerwacja.add(item)
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

    fun Usun(item: ItemRezerwacja) {
        if (!item.isEmptyItem) {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć?")
            alertDialogBuilder.setPositiveButton("Usuń") { dialog, which ->
                item.let {
                    try {
                        var query = "call usun_rezerwacje(\'${item.id_rezerwacji}\')"
                        val callableStatement: CallableStatement = connection?.prepareCall(query)!!
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
                    itemListRezerwacja.remove(it)
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

}
