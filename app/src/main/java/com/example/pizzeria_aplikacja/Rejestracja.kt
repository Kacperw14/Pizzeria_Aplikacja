package com.example.pizzeria_aplikacja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class Rejestracja : Fragment() {

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
            // Wykonaj odpowiednie działania po kliknięciu przycisku rejestracji
            registerUser()
        }
    }

    private fun registerUser() {

        val imie = view?.findViewById<EditText>(R.id.editTextImie)?.text.toString()
        val nazwisko = view?.findViewById<EditText>(R.id.editTextNazwisko)?.text.toString()
        val email = view?.findViewById<EditText>(R.id.editTextEmail)?.text.toString()
        val haslo = view?.findViewById<EditText>(R.id.editTextPassword)?.text.toString()
        val potwierdzHaslo =
            view?.findViewById<EditText>(R.id.editTextConfirmPassword)?.text.toString()
        val kod = 1234;

        // Sprawdź, czy pola są wypełnione
        if (email.isNotEmpty() && haslo.isNotEmpty() && haslo == potwierdzHaslo) {
            if (haslo == potwierdzHaslo) {
                lifecycleScope.launch {
                    sendConfirmationEmail(email.toString(), kod.toString())
                }
                //if(kod == editKod){}
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_rejestracja_to_login)
            } else Toast.makeText(requireContext(), "Podane hasło się różni", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
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
                var x = view?.findViewById<EditText>(R.id.editTextImie)
                x?.setText(e.toString())
                e.printStackTrace()
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
        } catch (e: MessagingException) {
            e.printStackTrace()
        }

    }

}