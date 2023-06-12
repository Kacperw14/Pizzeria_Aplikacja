package com.example.pizzeria_aplikacja

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController

var itemListDanie: MutableList<ItemDanie> = mutableListOf()

class Danie : Fragment() {
    private lateinit var adapter: DanieAdapter
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DanieAdapter(requireContext(), itemListDanie)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_danie, container, false)
        val button = view.findViewById<AppCompatImageButton>(R.id.imageButton5)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_danie_to_strona)
        }

        listView = view?.findViewById<ListView>(R.id.List)!!
        listView.adapter = adapter

        val button2 = view.findViewById<Button>(R.id.button7)
        button2.setOnClickListener {
            findNavController().navigate(R.id.action_danie_to_ustawienia)
        }
        val buttonPrzej = view.findViewById<Button>(R.id.buttonPrzej)
        buttonPrzej.setOnClickListener {
            findNavController().navigate(R.id.action_danie_to_skladnik)
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
            adapter.showParameterInputDialog(null)
            listView.adapter = adapter
            buttonDodajTran.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
        }
        return view
    }
}

data class ItemDanie(
    var id_dania: String,
    var nazwa_dania: String,
    var cena_dania: String,
    var rozmiar_pizzy: String,
    var czy_zawieraM: String,
    var czy_ostre: String,

    var iconResId: Int,
    var deleteIconResId: Int,
    val isEmptyItem: Boolean = false,
    var isEditing: Boolean = false
)