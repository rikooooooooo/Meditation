package com.example.meditation

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ArticleAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val articleImage: ImageView = view.findViewById(R.id.artikel_image)
        val articleTitle: TextView = view.findViewById(R.id.artikel_title)
        val articleAuthor: TextView = view.findViewById(R.id.artikel_author)
        val articleButton: LinearLayout = view.findViewById(R.id.artikel_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        println("onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artikel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("Number of articles 2: ${articles.size}")
        println("onBindViewHolder called for position: $position")
        val article = articles[position]

        holder.articleTitle.text = article.judulArtikel
        holder.articleAuthor.text = article.sumberArtikel
        Picasso.get().load(article.gambarArtikel).into(holder.articleImage)
        holder.articleButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, article_full::class.java)
            intent.putExtra("article", article)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = articles.size


}
