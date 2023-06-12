package com.example.pizzeria_aplikacja

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController


class Strona : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_strona, container, false)

        val raportButton = view.findViewById<Button>(R.id.button3)
        val rezerwacjaButton = view.findViewById<Button>(R.id.button2)
        val danieButton = view.findViewById<Button>(R.id.button)
        val button = view.findViewById<Button>(R.id.StronaUstawieniaButton)

        val szaryKolor = ContextCompat.getColor(requireContext(), R.color.gray)
        val pomaranczKolor = ContextCompat.getColor(requireContext(), R.color.orange)

        raportButton.backgroundTintList = ColorStateList.valueOf(szaryKolor)
        danieButton.backgroundTintList = ColorStateList.valueOf(szaryKolor)
        rezerwacjaButton.backgroundTintList = ColorStateList.valueOf(szaryKolor)
        raportButton?.isEnabled = false
        danieButton?.isEnabled = false
        rezerwacjaButton?.isEnabled = false

        when (zalogowany.stanowisko) {
            Stanowisko.KIEROWNIK -> {
                raportButton?.backgroundTintList = ColorStateList.valueOf(pomaranczKolor)
                raportButton?.isEnabled = true
            }

            Stanowisko.KUCHARZ -> {
                danieButton?.backgroundTintList = ColorStateList.valueOf(pomaranczKolor)
                danieButton?.isEnabled = true
            }

            Stanowisko.KELNER -> {
                rezerwacjaButton?.backgroundTintList = ColorStateList.valueOf(pomaranczKolor)
                rezerwacjaButton?.isEnabled = true
            }

            else -> null
        }

        button.setOnClickListener {
            findNavController().navigate(R.id.action_strona_to_ustawienia)
        }

        raportButton.setOnClickListener {
            findNavController().navigate(R.id.action_strona_to_raport)
        }

        rezerwacjaButton.setOnClickListener {
            findNavController().navigate(R.id.action_strona_to_rezerwacje)
        }

        danieButton.setOnClickListener {
            findNavController().navigate(R.id.action_strona_to_danie)
        }
        return view
    }
}