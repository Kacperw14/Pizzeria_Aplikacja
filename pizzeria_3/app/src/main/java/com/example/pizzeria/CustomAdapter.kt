package com.example.pizzeria
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.ImageButton
data class Item(
    val value1: String,
    val value2: String,
    val value3: String,
    val iconResId: Int,
    val iconResId2: Int
)
class CustomAdapter(context: Context, private val itemList: List<Item>) :
    ArrayAdapter<Item>(context, 0, itemList) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.list_item_layout, parent, false)
        }

        val textView1: TextView? = itemView?.findViewById(R.id.text_view1)
        val textView2: TextView? = itemView?.findViewById(R.id.text_view2)
        val textView3: TextView? = itemView?.findViewById(R.id.text_view3)
        val iconButton: ImageButton? = itemView?.findViewById(R.id.icon_button)
        val iconButton2: ImageButton? = itemView?.findViewById(R.id.icon_button_2)

        val item: Item? = getItem(position)
        item?.let {
            textView1?.text = item.value1
            textView2?.text = item.value2
            textView3?.text = item.value3
            iconButton?.setImageResource(R.drawable.ic_kosz_foreground)
            iconButton2?.setImageResource(R.drawable.ic_kosz_foreground)
            iconButton?.setOnClickListener {
                // Obsłuż kliknięcie IconButton
                Toast.makeText(context, "Kliknięto IconButton", Toast.LENGTH_SHORT).show()
            }
            iconButton2?.setOnClickListener {
                // Obsłuż kliknięcie IconButton
                Toast.makeText(context, "Kliknięto IconButton", Toast.LENGTH_SHORT).show()
            }
        }

        return itemView!!
    }
}