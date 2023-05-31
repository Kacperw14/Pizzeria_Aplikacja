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

class Login : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        val button = view.findViewById<Button>(R.id.button3)
        button.setOnClickListener{
            findNavController().navigate(R.id.action_login_to_rejestracja)
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            // Handle login button click event
            loginUser()
        }
    }

    private fun loginUser() {
        // Perform login logic here

        val editTextUsername = view?.findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = view?.findViewById<EditText>(R.id.editTextPassword)

        val username = editTextUsername?.text.toString()
        val password = editTextPassword?.text.toString()

        // Example login logic
        if (username == "admin" && password == "p") {
            findNavController().navigate(R.id.action_login_to_rejestracja)
            // Login success, navigate to next screen
            // Replace the code below with your own logic
            Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
        } else {
            // Login failed, show error message
            // Replace the code below with your own logic
            Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

}

