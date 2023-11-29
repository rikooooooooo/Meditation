package com.example.meditation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class user : Fragment() {
    private lateinit var dateTV: TextView
    private lateinit var calendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        // Use view.findViewById for fragment's layout
        val imageView: ImageView = view.findViewById(R.id.welcome_img)
        val logoutButton: Button = view.findViewById(R.id.logout_button)
        val editButton: Button = view.findViewById(R.id.edit_button)

        dateTV = view.findViewById(R.id.idTVDate)
        calendarView = view.findViewById(R.id.calendarView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$dayOfMonth-${month + 1}-$year"
            dateTV.text = date
        }
        logoutButton.setOnClickListener {
            startActivity(Intent(requireContext(), login::class.java))
        }

        return view
    }

}