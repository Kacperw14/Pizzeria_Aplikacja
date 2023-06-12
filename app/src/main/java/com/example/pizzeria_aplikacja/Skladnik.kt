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

var itemListSkladnik: MutableList<ItemSkladnik> = mutableListOf()

class Skladnik : Fragment() {
    private lateinit var adapter: SkladnikAdapter
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SkladnikAdapter(requireContext(), itemListSkladnik)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_skladnik, container, false)
        val button = view.findViewById<AppCompatImageButton>(R.id.imageButton5)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_skladnik_to_strona)
        }

        listView = view?.findViewById<ListView>(R.id.List)!!
        listView.adapter = adapter

        val button2 = view.findViewById<Button>(R.id.button7)
        button2.setOnClickListener {
            findNavController().navigate(R.id.action_skladnik_to_ustawienia)
        }

        val buttonPrzej = view.findViewById<Button>(R.id.buttonPrzej)
        buttonPrzej.setOnClickListener {
            findNavController().navigate(R.id.action_skladnik_to_danie)
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

data class ItemSkladnik(
    var id_skladnika: String,
    var nazwa_skladnika: String,
    var czy_ostre: String,
    var czy_wege: String,

    var iconResId: Int,
    var deleteIconResId: Int,
    val isEmptyItem: Boolean = false,
    var isEditing: Boolean = false
)