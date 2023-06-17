package com.example.pizzeria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import androidx.appcompat.widget.AppCompatImageButton
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView

class Raport : Fragment() {



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
        val listView: ListView = view.findViewById<ListView>(R.id.List)


        val itemList = listOf(
            Item("Wartość 1", "Wartość 2","Wartość 3", R.drawable.ic_imie_foreground,R.drawable.ic_imie_foreground),
            Item("Wartość 3", "Wartość 4", "wartosc 3",R.drawable.ic_inf_foreground,R.drawable.ic_imie_foreground),
            // Dodaj inne obiekty Item z różnymi ikonami
        )
        val adapter = CustomAdapter(requireContext(), itemList)
        listView.adapter = adapter


        return view
    }


}