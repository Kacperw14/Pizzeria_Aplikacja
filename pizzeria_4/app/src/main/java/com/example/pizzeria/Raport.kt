package com.example.pizzeria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.appcompat.widget.AppCompatImageButton
import java.io.Serializable
import androidx.lifecycle.ViewModel
import androidx.fragment.app.viewModels
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.graphics.Color
import android.view.inputmethod.InputMethodManager

//class RaportViewModel : ViewModel() {
//    val itemList: MutableList<Item> = mutableListOf(
//        Item("1", "200", "10.01.2023", R.drawable.ic_imie_foreground, R.drawable.ic_imie_foreground),
//        Item("2", "150", "5.06.2023", R.drawable.ic_inf_foreground, R.drawable.ic_imie_foreground),
//        // Dodaj inne obiekty Item z różnymi ikonami
//    )
//}
class Raport : Fragment() {
    private lateinit var itemList: MutableList<Item>
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicjalizacja elementów listy
        itemList = mutableListOf(
            Item("1", "100", "1.06.2023", R.drawable.ic_pytajnik_foreground, R.drawable.ic_kosz_foreground, R.drawable.ic_kosz_foreground, false),
            Item("2", "110", "2.06.2023", R.drawable.ic_pytajnik_foreground, R.drawable.ic_kosz_foreground, R.drawable.ic_kosz_foreground, false),
            // Dodaj inne obiekty Item z różnymi ikonami
        )

        // Inicjalizacja adaptera
        adapter = CustomAdapter(requireContext(), itemList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_raport, container, false)

        val button=view.findViewById<AppCompatImageButton>(R.id.imageButton5)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_raport_to_strona)
        }

        val button2=view.findViewById<Button>(R.id.button7)
        button2.setOnClickListener {
            findNavController().navigate(R.id.action_raport_to_ustawienia)
        }

        val listView: ListView = view.findViewById(R.id.List)

        // Ustawienie adaptera na liście
        listView.adapter = adapter

        val searchView: SearchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        val addButton=view.findViewById<Button>(R.id.button6)
        addButton.setOnClickListener {
            addButton.setBackgroundColor(Color.GRAY)

            adapter.addEmptyItem()

            listView.postDelayed({
                addButton.setBackgroundColor(resources.getColor(R.color.orange)) // Przywrócenie oryginalnego koloru przycisku
                if (adapter.isAddingItem()) {
                    // Ustawienie fokusu i otwarcie klawiatury dla nowego elementu
                    val newItemPosition = adapter.count - 1
                    listView.setSelection(newItemPosition)
                    listView.requestFocusFromTouch()
                    listView.smoothScrollToPosition(newItemPosition)
                    val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.showSoftInput(listView, InputMethodManager.SHOW_IMPLICIT)
                }
            }, 1000)
        }

        return view
    }
}



//class Item(
//    val value1: String,
//    val value2: String,
//    val value3: String,
//    val iconId1: Int,
//    val iconId2: Int
//) : Serializable
//
//class Raport : Fragment() {
//    private lateinit var itemList: MutableList<Item>
//    private lateinit var adapter: CustomAdapter
//
//    private val KEY_ITEM_LIST = "itemList"
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_raport, container, false)
//
//        val listView: ListView = view.findViewById(R.id.List)
//
//        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ITEM_LIST)) {
//            itemList = savedInstanceState.getSerializable(KEY_ITEM_LIST) as MutableList<Item>
//        } else {
//            // Inicjalizacja elementów listy
//            itemList = mutableListOf(
//                Item(
//                    "1",
//                    "200",
//                    "10.01.2023",
//                    R.drawable.ic_imie_foreground,
//                    R.drawable.ic_imie_foreground
//                ),
//                Item(
//                    "2",
//                    "150",
//                    "5.06.2023",
//                    R.drawable.ic_inf_foreground,
//                    R.drawable.ic_imie_foreground
//                ),
//                // Dodaj inne obiekty Item z różnymi ikonami
//            )
//        }
//
//        // Inicjalizacja adaptera
//        adapter = CustomAdapter(requireContext(), itemList)
//
//        // Ustawienie adaptera na liście
//        listView.adapter = adapter
//
//        val searchView: SearchView = view.findViewById(R.id.searchView)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter.filter.filter(newText)
//                return true
//            }
//        })
//
//        val button = view.findViewById<AppCompatImageButton>(R.id.imageButton5)
//        button.setOnClickListener {
//            findNavController().navigate(R.id.action_raport_to_strona)
//        }
//
//        val button2 = view.findViewById<Button>(R.id.button7)
//        button2.setOnClickListener {
//            findNavController().navigate(R.id.action_raport_to_ustawienia)
//        }
//
//        return view
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putSerializable(KEY_ITEM_LIST, ArrayList(itemList))
//    }
//}