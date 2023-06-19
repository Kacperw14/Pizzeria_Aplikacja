package com.example.pizzeria
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.ImageButton

import android.widget.Filter
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.view.inputmethod.InputMethodManager
data class Item(
    var value1: String,
    var value2: String,
    var value3: String,
    val iconResId: Int,
    val iconResId2: Int,
    val deleteIconResId: Int,
    val isEmptyItem: Boolean = false
)
//class CustomAdapter(context: Context, private val itemList: MutableList<Item>) :
//    ArrayAdapter<Item>(context, 0, itemList) {
//
//    private val inflater: LayoutInflater = LayoutInflater.from(context)
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var itemView = convertView
//        if (itemView == null) {
//            itemView = inflater.inflate(R.layout.list_item_layout, parent, false)
//        }
//
//        val textView1: TextView? = itemView?.findViewById(R.id.text_view1)
//        val textView2: TextView? = itemView?.findViewById(R.id.text_view2)
//        val textView3: TextView? = itemView?.findViewById(R.id.text_view3)
//        val iconButton: ImageButton? = itemView?.findViewById(R.id.icon_button)
//        val iconButton2: ImageButton? = itemView?.findViewById(R.id.icon_button_2)
//
//        val item: Item? = getItem(position)
//        item?.let {
//            textView1?.text = item.value1
//            textView2?.text = item.value2
//            textView3?.text = item.value3
//            iconButton?.setImageResource(R.drawable.ic_kosz_foreground)
//            iconButton2?.setImageResource(R.drawable.ic_kosz_foreground)
//            iconButton?.setOnClickListener {
//                // Obsłuż kliknięcie IconButton
//                Toast.makeText(context, "Kliknięto IconButton", Toast.LENGTH_SHORT).show()
//            }
//            iconButton2?.setOnClickListener {
//                // Obsłuż kliknięcie IconButton
//                itemList.removeAt(position)
//
//                notifyDataSetChanged()
//                Toast.makeText(context, "Usinięto transakcje", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        return itemView!!
//    }
//}
class CustomAdapter(context: Context, private val itemList: MutableList<Item>) :
    ArrayAdapter<Item>(context, 0, itemList) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var originalItemList: MutableList<Item> = ArrayList(itemList)
    private var filteredItemList: MutableList<Item> = ArrayList(itemList)
    private var isAddingItem: Boolean = false
    private val enteredTextMap: MutableMap<Int, String> = mutableMapOf()


    override fun getCount(): Int {
        return filteredItemList.size
    }

    override fun getItem(position: Int): Item? {
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

        val item: Item? = getItem(position)
        item?.let {
            if (item.isEmptyItem) {
                textView1?.setText("")
                textView2?.setText("")
                textView3?.setText("")
                textView1?.isEnabled = true
                textView2?.isEnabled = true
                textView3?.isEnabled = true// Włącz pole tekstowe
            } else {
                textView1?.setText(item.value1)
                textView2?.setText(item.value2)
                textView3?.setText(item.value3)
                textView1?.isEnabled = false
                textView2?.isEnabled = false
                textView3?.isEnabled = false// Wyłącz pole tekstowe
            }

            iconButton?.setImageResource(item.iconResId)
            iconButton2?.setImageResource(item.deleteIconResId)

            iconButton2?.setOnClickListener {
                if (!item.isEmptyItem) {
                    val alertDialogBuilder = AlertDialog.Builder(context)
                    alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć?")
                    alertDialogBuilder.setPositiveButton("Usuń") { dialog, which ->
                        item.let {
                            itemList.remove(it)
                            originalItemList.remove(it)
                            filteredItemList.remove(it)
                            notifyDataSetChanged()
                            Toast.makeText(context, "Element został usunięty", Toast.LENGTH_SHORT).show()
                        }
                    }
                    alertDialogBuilder.setNegativeButton("Anuluj") { dialog, which ->
                        // Anuluj usunięcie
                    }
                    alertDialogBuilder.show()
                }
            }


            iconButton?.setOnClickListener {
                Toast.makeText(context, "Kliknięto IconButton", Toast.LENGTH_SHORT).show()
            }

            textView1?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Niepotrzebna implementacja
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Zaktualizuj wartość w obiekcie Item
                    item.value1 = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    // Niepotrzebna implementacja
                }
            })
            textView2?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Niepotrzebna implementacja
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Zaktualizuj wartość w obiekcie Item
                    item.value2 = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    // Niepotrzebna implementacja
                }
            })
            textView3?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Niepotrzebna implementacja
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Zaktualizuj wartość w obiekcie Item
                    item.value3 = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    // Niepotrzebna implementacja
                }
            })
            textView1?.setText(item.value1)
            textView2?.setText(item.value2)
            textView3?.setText(item.value3)
        }


            return itemView!!
        }

        fun addEmptyItem() {
            val newItem = Item(
                "",
                "",
                "",
                R.drawable.ic_pytajnik_foreground,
                R.drawable.ic_kosz_foreground,
                R.drawable.ic_kosz_foreground,
                true
            )
            itemList.add(newItem)
            originalItemList.add(newItem)
            filteredItemList.add(newItem)
            notifyDataSetChanged()
        }

        fun isAddingItem(): Boolean {
            return isAddingItem
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
                            if (item.value1.toLowerCase().contains(filterPattern) ||
                                item.value2.toLowerCase().contains(filterPattern) ||
                                item.value3.toLowerCase().contains(filterPattern)
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
    }

//class CustomAdapter(context: Context, private val itemList: MutableList<Item>) :
//    ArrayAdapter<Item>(context, 0, itemList) {
//
//    private val originalItemList: MutableList<Item> = ArrayList(itemList)
//    private val filteredItemList: MutableList<Item> = ArrayList(itemList)
//    private val inflater: LayoutInflater = LayoutInflater.from(context)
//
//    override fun getCount(): Int {
//        return filteredItemList.size
//    }
//
//    override fun getItem(position: Int): Item? {
//        return itemList[position]
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var itemView = convertView
//        if (itemView == null) {
//            itemView = inflater.inflate(R.layout.list_item_layout, parent, false)
//        }
//
//        val textView1: TextView? = itemView?.findViewById(R.id.text_view1)
//        val textView2: TextView? = itemView?.findViewById(R.id.text_view2)
//        val textView3: TextView? = itemView?.findViewById(R.id.text_view3)
//        val iconButton: ImageButton? = itemView?.findViewById(R.id.icon_button)
//        val iconButton2: ImageButton? = itemView?.findViewById(R.id.icon_button_2)
//
//        val item: Item? = getItem(position)
//        item?.let {
//            textView1?.text = item.value1
//            textView2?.text = item.value2
//            textView3?.text = item.value3
//            iconButton?.setImageResource(R.drawable.ic_pytajnik_foreground)
//            iconButton2?.setImageResource(R.drawable.ic_kosz_foreground)
//
//            iconButton2?.setOnClickListener {
//                val alertDialogBuilder = AlertDialog.Builder(context)
//                alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć?")
//                alertDialogBuilder.setPositiveButton("Usuń") { dialog, which ->
//                    item.let {
//                        itemList.remove(it)
//                        originalItemList.remove(it)
//                        filteredItemList.remove(it)
//                        notifyDataSetChanged()
//                        Toast.makeText(context, "Element został usunięty", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                alertDialogBuilder.setNegativeButton("Anuluj") { dialog, which ->
//                    // Anuluj usunięcie
//                }
//                alertDialogBuilder.show()
//            }
//
//            iconButton?.setOnClickListener {
//                Toast.makeText(context, "Kliknięto IconButton", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        return itemView!!
//    }
//
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val filteredList = mutableListOf<Item>()
//
//                if (constraint.isNullOrEmpty()) {
//                    filteredList.addAll(originalItemList)
//                } else {
//                    val filterPattern = constraint.toString().toLowerCase().trim()
//                    for (item in originalItemList) {
//                        if (item.value1.toLowerCase().contains(filterPattern) ||
//                            item.value2.toLowerCase().contains(filterPattern) ||
//                            item.value3.toLowerCase().contains(filterPattern)
//                        ) {
//                            filteredList.add(item)
//                        }
//                    }
//                }
//
//                val filterResults = FilterResults()
//                filterResults.values = filteredList
//                return filterResults
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                filteredItemList.clear()
//                if (results != null && results.values is List<*>) {
//                    val filteredList = results.values as List<Item>
//                    filteredItemList.addAll(filteredList)
//                }
//                notifyDataSetChanged()
//            }
//        }
//    }
//}

