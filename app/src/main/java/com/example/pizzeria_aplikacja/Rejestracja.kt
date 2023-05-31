package com.example.pizzeria_aplikacja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController

class Rejestracja : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rejestracja, container, false)

        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener(){
            findNavController().navigate(R.id.action_rejestracja_to_login)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonRegister = view.findViewById<Button>(R.id.button4)
        buttonRegister.setOnClickListener {
            // Wykonaj odpowiednie działania po kliknięciu przycisku rejestracji
            registerUser()
        }
    }

    private fun registerUser() {
        val imie = view?.findViewById<EditText>(R.id.editTextImie)?.text.toString()
        val nazwisko = view?.findViewById<EditText>(R.id.editTextNazwisko)?.text.toString()
        val email = view?.findViewById<EditText>(R.id.editTextEmail)?.text.toString()
        val haslo = view?.findViewById<EditText>(R.id.editTextPassword)?.text.toString()
        val potwierdzHaslo = view?.findViewById<EditText>(R.id.editTextConfirmPassword)?.text.toString()

        // Sprawdź, czy pola są wypełnione
        if (email.isNotEmpty() && haslo.isNotEmpty() && haslo == potwierdzHaslo) {

            Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
        } else {
            // Wyświetl błąd, gdy pola są puste
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}