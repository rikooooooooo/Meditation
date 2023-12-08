package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso


class article_full : AppCompatActivity(){
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
            finish()
        }

        val intent = intent
        val article = intent.getSerializableExtra("article") as Article

        // Now you can use the article object to populate your views
        // For example:
        articleText1.text = article.judulArtikel
        articleText2.text = article.sumberArtikel
        textViewContent.text = article.kontenArtikel
        Picasso.get().load(article.gambarArtikel).into(artikelImg)
        // Load content into views
//        articleText1.text = "Meditate for Beginners"
//        articleText2.text = "By Riko Firmansyah"
//        textViewContent.text = getString(R.string.lorem)

    }
}