package com.example.pizzeria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.navigation.NavOptions


class Strona : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_strona, container, false)
        val button=view.findViewById<Button>(R.id.button4)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_strona_to_ustawienia)
        }
        val button2=view.findViewById<Button>(R.id.button3)
        button2.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.raport, false)
                .build()
            findNavController().navigate(R.id.action_strona_to_raport, null, navOptions)
        }
            //findNavController().navigate(R.id.action_strona_to_raport)
        //}
        return view
    }
    }