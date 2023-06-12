package com.example.pizzeria_aplikacja

import android.annotation.SuppressLint
import android.app.DownloadManager.Query
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
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class Ustawienia : Fragment() {
    private lateinit var nazwisko: String
    private lateinit var nazwiskoEditText: EditText
    private lateinit var imie: String
    private lateinit var imieEditText: EditText
    private lateinit var haslo: String
    private lateinit var hasloEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var button1ClickCount = 0
        var button2ClickCount = 0
        var button3ClickCount = 0
        var preparedStatement: PreparedStatement? = null
        var query: String = ""

        val view = inflater.inflate(R.layout.fragment_ustawienia, container, false)
        val sharedPreferences =
            requireContext().getSharedPreferences("Ustawienia", Context.MODE_PRIVATE)
        haslo = zalogowany.haslo
        val hasloEditText = view.findViewById<EditText>(R.id.hasloEditText)
        hasloEditText.setText(haslo)
        hasloEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        hasloEditText.isEnabled = false

        hasloEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                haslo = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        nazwisko = zalogowany.nazwisko
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
        imie = zalogowany.imie
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
        val textViewemail = view.findViewById<TextView>(R.id.textView7)
        textViewemail.text = zalogowany.email
        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = zalogowany.imie + ' ' + zalogowany.nazwisko

        val buttonust1 = view.findViewById<AppCompatImageButton>(R.id.imageButton3)
        buttonust1.setOnClickListener {
            val editor = sharedPreferences.edit()
            if (button3ClickCount < 1) {
                hasloEditText.isEnabled = true
                editor.putString("haslo", haslo)
                editor.apply()
                button3ClickCount++
            } else {
                try {
                    query = "UPDATE PRACOWNICY SET HASLO = \'$haslo\' WHERE HASLO = \'${zalogowany.haslo}\'"
                    preparedStatement = userConnection?.prepareStatement(query)

                    if (preparedStatement != null) {
                        preparedStatement!!.executeUpdate()
                        Toast.makeText(
                            requireContext(),
                            "Zmiana została dokonana",
                            Toast.LENGTH_SHORT
                        ).show()
                        userConnection?.commit()
                        zalogowany.haslo = haslo
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Operacja nie powiodła się",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                button3ClickCount = 0
                hasloEditText.isEnabled = false
            }
        }

        val buttonust2 = view.findViewById<AppCompatImageButton>(R.id.imageButton2)
        buttonust2.setOnClickListener {
            val editor = sharedPreferences.edit()
            if (button2ClickCount < 1) {
                nazwiskoEditText.isEnabled = true
                editor.putString("nazwisko", nazwisko)
                editor.apply()
                button2ClickCount++
            } else {
                try {
                     query = "UPDATE PRACOWNICY SET NAZWISKO = \'$nazwisko\' WHERE NAZWISKO = \'${zalogowany.nazwisko}\'"
                    preparedStatement = userConnection?.prepareStatement(query)

                    if (preparedStatement != null) {
                        preparedStatement!!.executeUpdate()
                        Toast.makeText(
                            requireContext(),
                            "Zmiana została dokonana",
                            Toast.LENGTH_SHORT
                        ).show()
                        userConnection?.commit()
                        zalogowany.nazwisko = nazwisko
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Operacja nie powiodła się",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                button2ClickCount = 0
                nazwiskoEditText.isEnabled = false
            }
        }

        val buttonust3 = view.findViewById<AppCompatImageButton>(R.id.imageButton)
        buttonust3.setOnClickListener {
            val editor = sharedPreferences.edit()
            if (button1ClickCount < 1) {
                imieEditText.isEnabled = true
                editor.putString("imie", imie)
                editor.apply()
                button1ClickCount++
            } else {
                try {
                    query = "UPDATE PRACOWNICY SET IMIE = \'$imie\' WHERE IMIE = \'${zalogowany.imie}\'"
                    preparedStatement = userConnection?.prepareStatement(query)

                    if (preparedStatement != null) {
                        preparedStatement!!.executeUpdate()
                        Toast.makeText(
                            requireContext(),
                            "Zmiana została dokonana",
                            Toast.LENGTH_SHORT
                        ).show()
                        userConnection?.commit()
                        zalogowany.imie = imie
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Operacja nie powiodła się",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                button1ClickCount = 0
                imieEditText.isEnabled = false
            }
        }

        val button = view.findViewById<AppCompatImageButton>(R.id.imageButton4)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_ustawienia_to_strona)
        }
        val buttonLogout = view.findViewById<Button>(R.id.button5)
        buttonLogout.setOnClickListener {
            logoutUser()
            Toast.makeText(requireContext(), "Wylogowano", Toast.LENGTH_SHORT).show()
        }
        return view
    }


    private fun logoutUser() {
        connection?.close()
        //userConnection?.close();
        findNavController().navigate(R.id.action_ustawienia_to_login)
        return
    }
}