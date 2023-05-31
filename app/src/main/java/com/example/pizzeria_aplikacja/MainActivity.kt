package com.example.pizzeria_aplikacja

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.ResultSet

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var navController: NavController
    private val driver: String = "oracle.jdbc.driver.OracleDriver"
    private val url = "jdbc:oracle:thin:@192.168.0.38:1521:orcl" //192.168.0.38
    private val user = "\"KELNER\""
    private val password = "\"1234\""

    private fun getConnection(): Connection? {
        var connection: Connection? = null
        Class.forName(driver)
        try {
            connection = DriverManager.getConnection(url, user, password)
        } catch (e: SQLException) {
            e.printStackTrace()
            textView.text = e.toString()
        }

        return connection
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        //navController = navHostFragment.navController

        val threadPolicy: StrictMode.ThreadPolicy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(threadPolicy)

        textView = findViewById(R.id.textView)

        val buttonConnect =
            findViewById<Button>(R.id.button1) // Replace R.id.button with the ID of your Button
        val buttonLogin =
            findViewById<Button>(R.id.button2) // Replace R.id.button with the ID of your Button

        buttonLogin.setOnClickListener {
            textView.text = "Login"
            //val intent = Intent(this, Login::class.java)
            //startActivity(intent)

            //val navController = Navigation.findNavController()
            //val navController = Navigation.findNavController(this, R.id.nav_graph)
            //navController.navigate(R.id.logowanie)
        }

        buttonConnect.setOnClickListener {
            buttonClick()
        }
    }

    private fun buttonClick() {
        var result = ""
        val connection: Connection?
        try {
            connection = getConnection()

            if (connection != null) {
                //result = "connected"
                Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show()
                // Zapytanie SQL
                val query = "SELECT * FROM STOLIK"
                try {
                    val preparedStatement = connection.prepareStatement(query)

                    // Wykonanie zapytania
                    val resultSet: ResultSet = preparedStatement.executeQuery()

                    // Tworzenie łańcucha znaków z wynikami
                    while (resultSet.next()) {
                        val tableId = resultSet.getInt("id_stolika")
                        val tableNumber = resultSet.getInt("liczba_miejsc")
                        result += "Stolik: $tableId, Numer: $tableNumber\n"
                    }

                    // Zamykanie połączenia i zasobów
                    resultSet.close()
                    preparedStatement.close()
                } catch (e: SQLException) {
                    result = e.toString()
                }
            } else Toast.makeText(this, "Not connected!", Toast.LENGTH_SHORT).show()

            connection?.close()

        } catch (e: SQLException) {
            e.printStackTrace()
            //result = "Błąd pobierania danych z bazy danych."
        }
        textView.text = result
    }

}