package com.example.meditation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException



class learn : Fragment() {

    private lateinit var imageView1: ImageView
    private lateinit var articleTitle: TextView
    private lateinit var articleAuthor: TextView
    private lateinit var learnText: TextView
    private lateinit var articles: List<Article>
    private lateinit var materialButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_learn, container, false)

        // Initialize views
        imageView1 = view.findViewById(R.id.artikel_img)
        articleTitle = view.findViewById(R.id.learn_text2)
        articleAuthor = view.findViewById(R.id.learn_text3)
        learnText = view.findViewById(R.id.learn_text1)

        val articleButton = view.findViewById<Button>(R.id.button_article)

        articleButton.setOnClickListener {
            openNewPage()
        }

        // Fetch data from the API
        fetchDataFromAPI()

        return view
    }

    private fun openNewPage() {
        startActivity(Intent(requireContext(), article::class.java))
    }

    private fun parseArticles(jsonArray: JSONArray): List<Article> {
        val articles = mutableListOf<Article>()

        for (i in 0 until jsonArray.length()) {
            val articleJson = jsonArray.getJSONObject(i)
            val article = Article(
                articleJson.getString("judul_artikel"),
                articleJson.getString("sumber_artikel"),
                articleJson.getString("gambar_artikel")
            )
            articles.add(article)
        }

        return articles
    }
    private fun displayArticle(index: Int) {
        if (index < articles.size) {
            val article = articles[index]

            // Update UI elements
            articleTitle.text = article.judulArtikel
            articleAuthor.text = article.sumberArtikel

            // Load image using your preferred image loading library (e.g., Picasso, Glide)
            // Example using Glide:
            Picasso.get().load(article.gambarArtikel).into(imageView1)
        }
    }
    private fun fetchDataFromAPI() {
        val url = "https://forprojectk.000webhostapp.com/api-artikel.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                try {
                    // Check if the response is not empty
                    if (response.length() > 0) {
                        // Parse the JSON array into a list of articles
                        articles = parseArticles(response)

                        // Display the first article
                        displayArticle(0)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError? ->
                // Handle errors, e.g., display an error message
                error?.printStackTrace()
            }
        )

        // Add the request to the request queue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest)
    }
}

data class Article(val judulArtikel: String, val sumberArtikel: String, val gambarArtikel: String)