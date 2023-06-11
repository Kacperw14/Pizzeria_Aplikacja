package com.example.pizzeria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.appcompat.widget.AppCompatImageButton

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
        return view
    }


}