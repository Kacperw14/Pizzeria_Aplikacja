package com.example.pizzeria_aplikacja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

var czySzukac: Boolean = true
private val driver = "oracle.jdbc.driver.OracleDriver"
private val url = "jdbc:oracle:thin:@192.168.0.38:1521:orcl"
private var user = ""
private val password = "\"1234\""

public fun getConnection(user: String): Connection? {
    var connection: Connection? = null
    Class.forName(driver)
    try {
        connection = DriverManager.getConnection(url, user, password)
    } catch (e: SQLException) {
        e.printStackTrace()
    }
    return connection
}

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val imageView = view.findViewById<ImageView>(R.id.image1)
        val button = view.findViewById<Button>(R.id.button3)
        imageView.setImageResource(R.drawable.pizza)

        button.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_rejestracja)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (czySzukac) {
            Konto.Szukaj()
        }

        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        Toast.makeText(requireContext(), "Proszę czekać", Toast.LENGTH_SHORT).show()

        val email = view?.findViewById<EditText>(R.id.editTextUsername)?.text.toString()
        val haslo = view?.findViewById<EditText>(R.id.editTextPassword)?.text.toString()

        for (konto in uzytkownicy) {
            if (email == konto.email && haslo == konto.haslo) {
                val connection: Connection?
                try {
                    when (konto.stanowisko) {
                        Stanowisko.KIEROWNIK -> {
                            user = "\"KIEROWNIK\""
                        }

                        Stanowisko.KUCHARZ -> {
                            user = "\"KUCHARZ\""
                        }

                        Stanowisko.KELNER -> {
                            user = "\"KELNER\""
                        }

                        else -> {}
                    }
                    connection = getConnection(user)
                    if (connection != null) {
                        Toast.makeText(requireContext(), "Połączono jako $user", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.action_login_to_rejestracja) //TODO zmiana na homepage
                        return
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Połączenie z bazą danych nie powiodło się", Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: SQLException) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        Toast.makeText(
            requireContext(),
            "Logowanie nie powiodło się, błędny login lub hasło",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}


//connection?.close() //TODO zamknij connect
