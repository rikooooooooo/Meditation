package com.example.meditation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import java.io.Serializable


class learn : Fragment() {

//    private lateinit var imageView1: ImageView
//    private lateinit var articleTitle: TextView
//    private lateinit var articleAuthor: TextView
    private lateinit var learnText: TextView
    private lateinit var articles: List<Article>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_learn, container, false)

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view)
        // Initialize views
//        imageView1 = view.findViewById(R.id.artikel_image)
//        articleTitle = view.findViewById(R.id.artikel_title)
//        articleAuthor = view.findViewById(R.id.artikel_author)
        learnText = view.findViewById(R.id.learn_text1)
//
//        val articleButton = view.findViewById<Button>(R.id.button_article)

//        articleButton.setOnClickListener {
//            openNewPage()
//        }

        // Fetch data from the API
        fetchDataFromAPI()
        return view
    }

    private fun openNewPage() {
        startActivity(Intent(requireContext(), article_full::class.java))
    }

    private fun parseArticles(jsonArray: JSONArray): List<Article> {
        val articles = mutableListOf<Article>()

        for (i in 0 until jsonArray.length()) {
            val articleJson = jsonArray.getJSONObject(i)
            val article = Article(
                articleJson.getString("id_artikel"),
                articleJson.getString("judul_artikel"),
                articleJson.getString("konten_artikel"),
                articleJson.getString("sumber_artikel"),
                articleJson.getString("gambar_artikel")
            )
            articles.add(article)
        }
        return articles
    }

//    private fun displayArticle(index: Int) {
//        if (index < articles.size) {
//            val article = articles[index]
//
//            // Update UI elements
//            articleTitle.text = article.judulArtikel
//            articleAuthor.text = article.sumberArtikel
//
//            // Load image
//            Picasso.get().load(article.gambarArtikel).into(imageView1)
//        }
//    }

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
                        println("Number of articles: ${articles.size}") // Print the number of articles

                        // Create the adapter
                        val adapter = ArticleAdapter(articles)

                        // Set the adapter to the RecyclerView
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.adapter = adapter
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
data class Article(val idArtikel : String, val judulArtikel: String, val kontenArtikel: String,val sumberArtikel: String, val gambarArtikel: String) : Serializable