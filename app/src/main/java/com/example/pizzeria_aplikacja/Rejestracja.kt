package com.example.pizzeria_aplikacja

import android.app.DownloadManager.Query
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

enum class Stanowisko {
    BRAK,
    KIEROWNIK,
    KUCHARZ,
    KELNER
}

class Konto(
    _imie: String,
    _nazwisko: String,
    _email: String,
    _haslo: String,
    _stanowisko: Int
) : Fragment() {
    var imie: String = _imie
    var nazwisko: String = _nazwisko
    var email: String = _email
    var haslo: String = _haslo
    var stanowisko: Stanowisko = Stanowisko.values()[_stanowisko]

    companion object {
        fun Szukaj() { //TODO szukaj po usunieciu uzytkownika
            try {
                val user = "\"UZYTKOWNICY\""
                val connection = getConnection(user)
                if (connection != null) {
                    val sql = mutableListOf<String>()
                    for (i in 1 until 3) {
                        sql.add("SELECT IMIE FROM PRACOWNICY WHERE ID_PRACOWNIKA = $i")
                        sql.add("SELECT NAZWISKO FROM PRACOWNICY WHERE ID_PRACOWNIKA = $i")
                        sql.add("SELECT EMAIL FROM PRACOWNICY WHERE ID_PRACOWNIKA = $i")
                        sql.add("SELECT HASLO FROM PRACOWNICY WHERE ID_PRACOWNIKA = $i")
                        sql.add("SELECT STANOWISKO FROM PRACOWNICY WHERE ID_PRACOWNIKA = $i")
                    }
                    val parametry = mutableListOf<String>()
                    parametry.add("IMIE")
                    parametry.add("NAZWISKO")
                    parametry.add("EMAIL")
                    parametry.add("HASLO")
                    parametry.add("STANOWISKO")
                    val dane = mutableListOf<String>()
                    val stanowisko: Int = 1
                    try {
                        var statement: Statement? = null
                        var resultSet: ResultSet? = null
                         for (s in 0 until sql.size step 5) {
                             for (i in parametry.indices) {
                                 statement = connection.prepareStatement(sql[i + s])
                                 resultSet = statement.executeQuery()
                                 if (resultSet.next()) {
                                     dane.add(resultSet.getString(parametry[i]))
                                 }
                             }
                             if (statement == null || resultSet == null || dane.size == 0) {
                                 break
                             } else {
                                 uzytkownicy.add(
                                     Konto(
                                         dane[0 + s],
                                         dane[1 + s],
                                         dane[2 + s],
                                         dane[3 + s],
                                         stanowisko
                                     )
                                 )
                                 dane.clear()
                             }
                         }
                        resultSet?.close()
                        statement?.close()
                        connection.close()

                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }

                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            czySzukac = false
        }

    }
}

var uzytkownicy: MutableList<Konto> = mutableListOf()

class Rejestracja : Fragment() {

    lateinit var imie: String
    lateinit var nazwisko: String
    lateinit var email: String
    lateinit var haslo: String
    lateinit var potwierdzHaslo: String
    var kod = 1234
    var wyborStanowiska: Stanowisko = Stanowisko.BRAK


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rejestracja, container, false)

        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_rejestracja_to_login)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonRegister = view.findViewById<Button>(R.id.button4)
        buttonRegister.setOnClickListener {
            registerUser()
        }
        val kelnerButton = view.findViewById<Button>(R.id.button5)
        val kucharzButton = view.findViewById<Button>(R.id.button6)
        val kierownikButton = view.findViewById<Button>(R.id.button7)

        val szaryKolor = ContextCompat.getColor(requireContext(), R.color.gray)
        val nowyKolor = ContextCompat.getColor(requireContext(), R.color.orange)

        kelnerButton.setOnClickListener() {
            wyborStanowiska = Stanowisko.KELNER
            kelnerButton.backgroundTintList = ColorStateList.valueOf(nowyKolor)
            kucharzButton.backgroundTintList = ColorStateList.valueOf(szaryKolor)
            kierownikButton.backgroundTintList = ColorStateList.valueOf(szaryKolor) //TODO ramka znika
        }
        kucharzButton.setOnClickListener() {
            wyborStanowiska = Stanowisko.KUCHARZ
            kucharzButton.backgroundTintList = ColorStateList.valueOf(nowyKolor)
            kelnerButton.backgroundTintList = ColorStateList.valueOf(szaryKolor)
            kierownikButton.backgroundTintList = ColorStateList.valueOf(szaryKolor)
        }
        kierownikButton.setOnClickListener() {
            wyborStanowiska = Stanowisko.KIEROWNIK
            kierownikButton.backgroundTintList = ColorStateList.valueOf(nowyKolor)
            kucharzButton.backgroundTintList = ColorStateList.valueOf(szaryKolor)
            kelnerButton.backgroundTintList = ColorStateList.valueOf(szaryKolor)
        }
    }

    private fun registerUser() {
        val connection: Connection?
        imie = view?.findViewById<EditText>(R.id.editTextImie)?.text.toString()
        nazwisko = view?.findViewById<EditText>(R.id.editTextNazwisko)?.text.toString()
        email = view?.findViewById<EditText>(R.id.editTextEmail)?.text.toString()
        haslo = view?.findViewById<EditText>(R.id.editTextPassword)?.text.toString()
        potwierdzHaslo =
            view?.findViewById<EditText>(R.id.editTextConfirmPassword)?.text.toString()
        if (email.isNotEmpty() && haslo.isNotEmpty() && imie.isNotEmpty() && nazwisko.isNotEmpty() && potwierdzHaslo.isNotEmpty() && wyborStanowiska != Stanowisko.BRAK) {
            Toast.makeText(requireContext(), "Proszę czekać", Toast.LENGTH_SHORT).show()
            if (haslo == potwierdzHaslo) {

                lifecycleScope.launch {
                    sendConfirmationEmail(email, kod.toString())
                }
                //if(kod == editKod){}//TODO sprawdz kod email

                var kod = 1234
                try {
                    val user = "\"UZYTKOWNICY\""
                    val connection = getConnection(user)
                    if (connection != null) {
                        val sql =
                            "BEGIN \"DODAJ_PRACOWNIKA\"('$imie', '$nazwisko', '$email', '$haslo', ${wyborStanowiska.ordinal}); END;"
                        val statement = connection.prepareStatement(sql)
                        try {
                            statement.execute()
                        } catch (e: SQLException) {
                            e.printStackTrace()
                        }
                        connection.close()
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
                uzytkownicy.add(Konto(imie, nazwisko, email, haslo, wyborStanowiska.ordinal))

                Toast.makeText(requireContext(), "Rejestracja powiodła się!", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_rejestracja_to_login)
            } else {
                Toast.makeText(requireContext(), "Podane hasło się różni", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(requireContext(), "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendConfirmationEmail(email: String, confirmationCode: String) {
        val properties = System.getProperties()
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true" // Use TLS encryption

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(
                    "wesolapizzeria@gmail.com",
                    "ewlrvtbhyssomiot"
                )
            }
        })
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress("wesolapizzeria@gmail.com"))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            message.subject = "Potwierdź swoje hasło!"
            message.setText("Twój kod to: $confirmationCode")
            try {
                Transport.send(message)
            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        } catch (e: MessagingException) {
            e.printStackTrace()
        }

    }

}