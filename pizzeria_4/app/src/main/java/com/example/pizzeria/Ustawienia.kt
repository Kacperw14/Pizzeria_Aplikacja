package com.example.pizzeria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.appcompat.widget.AppCompatImageButton
import android.widget.EditText
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import android.content.Context
import android.text.InputType
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import android.widget.Toast
class Ustawienia : Fragment() {


    private lateinit var nazwisko: String
    private lateinit var nazwiskoEditText: EditText
    private lateinit var imie: String
    private lateinit var imieEditText: EditText
    private lateinit var haslo: String
    private lateinit var hasloEditText: EditText
    //private val driver = "oracle.jdbc.driver.OracleDriver"
   // private val url = "jdbc:oracle:thin:@192.168.0.38:1521:orcl"
    private val user = "\"KELNER\""
    private val password = "\"1234\""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ustawienia, container, false)
        val sharedPreferences =
            requireContext().getSharedPreferences("Ustawienia", Context.MODE_PRIVATE)
        haslo = sharedPreferences.getString("haslo", "") ?: ""
        val hasloEditText = view.findViewById<EditText>(R.id.hasloEditText)
        hasloEditText.setText(haslo)
        hasloEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        hasloEditText.isEnabled = false

        hasloEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                haslo = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        nazwisko = sharedPreferences.getString("nazwisko", "") ?: ""
        nazwiskoEditText = view.findViewById<EditText>(R.id.nazwiskoEditText)
        nazwiskoEditText.setText(nazwisko)
        nazwiskoEditText.isEnabled = false
        nazwiskoEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nazwisko = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        val textViewnazw = view.findViewById<TextView>(R.id.textView7)
        textViewnazw.text = nazwisko
        imie = sharedPreferences.getString("imie", "") ?: ""
        val imieEditText = view.findViewById<EditText>(R.id.imieEditText)
        imieEditText.setText(imie)
        imieEditText.isEnabled = false

        imieEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imie = s.toString()

            }

            override fun afterTextChanged(s: Editable?) {}
        })
        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = imie
        val buttonust1 = view.findViewById<AppCompatImageButton>(R.id.imageButton3)
        buttonust1.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("haslo", haslo)
            editor.apply()
            hasloEditText.isEnabled = true
        }
        val buttonust2 = view.findViewById<AppCompatImageButton>(R.id.imageButton2)
        buttonust2.setOnClickListener {
            nazwiskoEditText.isEnabled = true
            val editor = sharedPreferences.edit()
            editor.putString("nazwisko", nazwisko)
            editor.apply()
        }
        val buttonust3 = view.findViewById<AppCompatImageButton>(R.id.imageButton)
        buttonust3.setOnClickListener {

            imieEditText.isEnabled = true
            val editor = sharedPreferences.edit()
            editor.putString("imie", imie)
            editor.apply()
        }


        val button = view.findViewById<AppCompatImageButton>(R.id.imageButton4)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_ustawienia_to_strona)
        }
        val buttonLogout = view.findViewById<Button>(R.id.button5)
        buttonLogout.setOnClickListener {
           // logoutUser()
            Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show()
        }
        return view
    }


//    private fun getConnection(): Connection? {
//        var connection: Connection? = null
//        Class.forName(driver)
//        try {
//            connection = DriverManager.getConnection(url, user, password)
//        } catch (e: SQLException) {
//            e.printStackTrace()
//        }
//        return connection
//    }

//    private fun logoutUser() {
//        val connection: Connection?
//        try {
//            connection = getConnection()
//            if (connection != null) {
//                // Wykonaj czynności wylogowania użytkownika
//
//                // Zamknij połączenie
//                connection.close()
//            }
//        } catch (e: SQLException) {
//            e.printStackTrace()
//        }
//        Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show()
//
//        // Przekieruj użytkownika na stronę logowania
//
//    }

}