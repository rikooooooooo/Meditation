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
import com.google.android.material.button.MaterialButton


class learn : Fragment() {

    private lateinit var imageView1: ImageView
    private lateinit var articleTitle: TextView
    private lateinit var articleAuthor: TextView
    private lateinit var learnText: TextView
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

        return view
    }

    private fun openNewPage() {
        startActivity(Intent(requireContext(), article::class.java))
    }



}