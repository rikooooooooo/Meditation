package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class article : AppCompatActivity(){
    public lateinit var articleContent: String
    private lateinit var backButton: ImageButton
    private lateinit var artikelImg: ImageView
    private lateinit var articleText1: TextView
    private lateinit var articleText2: TextView
    private lateinit var textViewContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article)

        backButton = findViewById(R.id.back_button)
        artikelImg = findViewById(R.id.artikel_img)
        articleText1 = findViewById(R.id.article_text1)
        articleText2 = findViewById(R.id.article_text2)
        textViewContent = findViewById(R.id.textViewContent)

        backButton.setOnClickListener {
            val intent = Intent(this, learn::class.java)
            startActivity(intent)
        }

        // Load content into views
        articleText1.text = "Meditate for Beginners"
        articleText2.text = "By Riko Firmansyah"
        textViewContent.text = getString(R.string.lorem)

    }
}