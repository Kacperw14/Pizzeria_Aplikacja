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
import java.util.Calendar
import java.util.Date

class DanieAdapter(
    context: Context,
    private val itemListDanie: MutableList<ItemDanie>
) :
    ArrayAdapter<ItemDanie>(context, 0, itemListDanie) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var originalItemList: MutableList<ItemDanie> = ArrayList(itemListDanie)
    private var filteredItemList: MutableList<ItemDanie> = ArrayList(itemListDanie)
    private var isAddingItem: Boolean = false
    private val enteredTextMap: MutableMap<Int, String> = mutableMapOf()
    var ID_DANIA: Int = 0
    var NAZWA_DANIA: String = ""
    var CENA_DANIA: Int = 0
    var ROZMIAR_PIZZY: Int = 0
    var CZY_ZAWIERA_MIESO: Int = 0
    var CZY_OSTRE: Int = 0

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

    override fun getItem(position: Int): ItemDanie? {
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

        val item: ItemDanie? = getItem(position)
        item?.let {
            textView1?.setText(item.id_dania)
            textView2?.setText(item.nazwa_dania)
            textView3?.setText(item.cena_dania)
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
                    param1TextView.setText("Numer: " + item.id_dania.toString())
                    param2TextView.setText("Nazwa: " + item.nazwa_dania.toString())
                    param3TextView.setText("Cena: " + item.cena_dania.toString())
                    param4TextView.setText("Rozmiar: " + item.rozmiar_pizzy)
                    param5TextView.setText("Czy zawiera mięso: " + item.czy_zawieraM)
                    param6TextView.setText("Czy jest ostre: " + item.czy_ostre)

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

    fun showParameterInputDialog(item: ItemDanie?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edytuj informacje o daniu") //TODO zmień

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

        param3DatePicker.visibility   = View.GONE
        param4DatePicker.visibility  = View.GONE
        paramtimePicker1.visibility = View.GONE
        paramtimePicker2.visibility = View.GONE

        param1EditText.setHint("Podaj nazwę dania")
        param2EditText.setHint("Podaj cenę dania")
        param5EditText.setHint("Podaj rozmiar pizzy")
        param6EditText.setHint("Podaj czy danie zawiera mięso")
        param7EditText.setHint("Podaj czy danie jest ostre") //TODO na początku hint to samo w rezer

        if (item != null) {
            param1EditText.setText(item.nazwa_dania)
            param2EditText.setText(item.cena_dania)
            param5EditText.setText(item.rozmiar_pizzy)
            param6EditText.setText(item.czy_zawieraM)
            param7EditText.setText(item.czy_ostre)
        }

        builder.setPositiveButton("OK") { _: DialogInterface, _: Int ->

            NAZWA_DANIA = try {
                param1EditText.text.toString()
            } catch (e: Exception) {
                ""
            }
            CENA_DANIA = try {
                param2EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }

            ROZMIAR_PIZZY = try {
                param5EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }
            CZY_ZAWIERA_MIESO = try {
                param6EditText.text.toString().toInt()
            } catch (e: Exception) {
                1
            }

            CZY_OSTRE = try {
                param7EditText.text.toString().toInt()
            } catch (e: Exception) {
                1 //TODO 0
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
            val query = "call DODAJ_DANIE(?, ?, ?, ?, ?, SYS.ODCINUMBERLIST(1))" //TODO
            val callableStatement: CallableStatement = connection?.prepareCall(query)!!
            callableStatement.setString(1, NAZWA_DANIA)
            callableStatement.setInt(2, CENA_DANIA)
            callableStatement.setInt(3, ROZMIAR_PIZZY)
            callableStatement.setInt(4, CZY_ZAWIERA_MIESO)
            callableStatement.setInt(5, CZY_OSTRE)
            callableStatement.execute()
            wczytajtransakcje(context)

            notifyDataSetChanged()
            connection?.commit()
            callableStatement.close()
            Toast.makeText(
                context,
                "Operacja dodania powiodła się",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: SQLException) {
            e.printStackTrace()
            Toast.makeText(
                context,
                "Błędne dane! Operacja nie powiodła się",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun update(item: ItemDanie) {
        try {
            val query =
                "UPDATE DANIE SET NAZWA_DANIA = ?, CENA_DANIA = ?, ROZMIAR_PIZZY = ?, CZY_ZAWIERA_MIESO = ?, CZY_OSTRE = ? WHERE ID_DANIA = ${item.id_dania}"
            val statement = connection?.prepareStatement(query)
            statement?.setString(1, NAZWA_DANIA)
            statement?.setInt(2, CENA_DANIA)
            statement?.setInt(3, ROZMIAR_PIZZY)
            statement?.setInt(4, CZY_ZAWIERA_MIESO)
            statement?.setInt(5, CZY_OSTRE)
            statement?.executeUpdate()
            wczytajtransakcje(context)
            notifyDataSetChanged()

            connection?.commit()
            statement?.close()
            Toast.makeText(
                context,
                "Operacja edycji powiodła się",
                Toast.LENGTH_SHORT
            ).show()
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
                val filteredList = mutableListOf<ItemDanie>()

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(originalItemList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in originalItemList) {
                        if (item.id_dania.toLowerCase().contains(filterPattern)
                            || item.nazwa_dania.toLowerCase().contains(filterPattern)
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
                    val filteredList = results.values as List<ItemDanie>
                    filteredItemList.addAll(filteredList)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun wczytajtransakcje(context: Context) {
        try {
            itemListDanie.clear()
            originalItemList.clear()
            filteredItemList.clear()
            val statement = connection?.createStatement()
            val query = "SELECT * FROM DANIE"
            val resultSet = statement?.executeQuery(query)
            lateinit var item: ItemDanie
            if (resultSet != null) {
                while (resultSet.next()) {
                    ID_DANIA = resultSet.getInt("ID_DANIA")
                    NAZWA_DANIA = resultSet.getString("NAZWA_DANIA")
                    CENA_DANIA = resultSet.getInt("CENA_DANIA")
                    ROZMIAR_PIZZY = resultSet.getInt("ROZMIAR_PIZZY")
                    CZY_ZAWIERA_MIESO = resultSet.getInt("CZY_ZAWIERA_MIESO")
                    CZY_OSTRE = resultSet.getInt("CZY_OSTRE")

                    item = ItemDanie(
                        ID_DANIA.toString(),
                        NAZWA_DANIA,
                        CENA_DANIA.toString(),
                        ROZMIAR_PIZZY.toString(),
                        CZY_ZAWIERA_MIESO.toString(),
                        CZY_OSTRE.toString(),

                        R.drawable.ic_pytajnik_foreground,
                        R.drawable.ic_pen_foreground,
                        false
                    )
                    itemListDanie.add(item)
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

    fun Usun(item: ItemDanie) {
        if (!item.isEmptyItem) {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć?")
            alertDialogBuilder.setPositiveButton("Usuń") { dialog, which ->
                item.let {
                    try {
                        var query = "call USUN_DANIE(${item.id_dania})"
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
                    itemListDanie.remove(it)
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
