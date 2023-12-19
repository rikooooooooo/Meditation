package com.example.meditation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class started  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.started)

        val welcomeImg2 = findViewById<ImageView>(R.id.welcome_img2)
        val startedText1 = findViewById<TextView>(R.id.started_text1)
        val startedText2 = findViewById<TextView>(R.id.started_text2)
        val startedText3 = findViewById<TextView>(R.id.started_text3)
        val startedButton = findViewById<Button>(R.id.started_button)

        // Add any logic or event listeners here as needed
        val storedUsername = getStoredUsername()
        val greeting = "Hi"
        val message = "\n Welcome to Meditation"
        if (storedUsername != null) {
            startedText2.text = "$greeting, $storedUsername, $message"
        } else {
            startedText2.text = "$greeting, $message"
        }
        startedButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    private fun getStoredUsername(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }
}