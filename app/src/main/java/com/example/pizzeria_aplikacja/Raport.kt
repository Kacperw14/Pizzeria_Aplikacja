package com.example.pizzeria_aplikacja

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

var itemList: MutableList<Item> = mutableListOf()

class Raport : Fragment() {
    private lateinit var adapter: CustomAdapter
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicjalizacja adaptera
        adapter = CustomAdapter(requireContext(), itemList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_raport, container, false)

        val button = view.findViewById<AppCompatImageButton>(R.id.imageButton5)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_raport_to_strona)
        }

        listView = view?.findViewById<ListView>(R.id.List)!!
        listView.adapter = adapter

        val button2 = view.findViewById<Button>(R.id.button7)
        button2.setOnClickListener {
            findNavController().navigate(R.id.action_raport_to_ustawienia)
        }

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
        val buttonDodajTran = view.findViewById<Button>(R.id.button6)
        buttonDodajTran.setOnClickListener {
            buttonDodajTran.setBackgroundColor(Color.GRAY)
            adapter.showParameterInputDialog()
            listView.adapter = adapter
            buttonDodajTran.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
        }
        return view
    }
}

data class Item(
    var id_transakcji: String,
    var id_stolika: String,
    var id_klienta: String,
    var dataRozpoczecia: String,
    var dataZakonczenia: String,
    var sposobPlatnosci: String,
    var kwota: String,
    val iconResId: Int,
    val iconResId2: Int,
    val deleteIconResId: Int,
    val isEmptyItem: Boolean = false
)