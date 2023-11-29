package com.example.meditation

import android.content.Intent
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
        startedButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}