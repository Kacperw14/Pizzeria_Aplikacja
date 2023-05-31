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
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Login : Fragment() {

    private val driver = "oracle.jdbc.driver.OracleDriver"
    private val url = "jdbc:oracle:thin:@192.168.0.38:1521:orcl"
    private val user = "\"KELNER\""
    private val password = "\"1234\""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val button = view.findViewById<Button>(R.id.button3)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_rejestracja)
        }
        return view
    }

    private fun getConnection(): Connection? {
        var connection: Connection? = null
        Class.forName(driver)
        try {
            connection = DriverManager.getConnection(url, user, password)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return connection
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {

        val email = view?.findViewById<EditText>(R.id.editTextUsername)?.text.toString()
        val haslo = view?.findViewById<EditText>(R.id.editTextPassword)?.text.toString()


        // Example login logic
        //if (email == "admin" && haslo == "p") {
        if (true) {
            val connection: Connection?
            try {
                connection = getConnection()
                if (connection != null) {
                    Toast.makeText(requireContext(), "Connected", Toast.LENGTH_SHORT).show()
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_login_to_rejestracja)
                } else Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
                connection?.close()
            } catch (e: SQLException) {
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}

