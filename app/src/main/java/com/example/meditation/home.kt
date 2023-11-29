package com.example.meditation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction


public class home : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val homeText1 = view?.findViewById<TextView>(R.id.home_text1)
        val homeText2 = view?.findViewById<TextView>(R.id.home_text2)
        val homeText3 = view?.findViewById<TextView>(R.id.home_text3)
        val learnButton = view.findViewById<Button>(R.id.learn_button)
        val musicButton = view.findViewById<Button>(R.id.music_button)
        val homeText4 = view?.findViewById<TextView>(R.id.home_text4)
        val homeText5 = view?.findViewById<TextView>(R.id.home_text5)
        val meditationButton = view.findViewById<Button>(R.id.meditation_button)

        learnButton.setOnClickListener {
            replaceFragment(learn(), R.id.learn)
        }

        musicButton.setOnClickListener {
            replaceFragment(music(), R.id.music)
        }

        meditationButton.setOnClickListener {
            startActivity(Intent(requireContext(), meditation::class.java))
        }
        return view
    }
    public fun replaceFragment(fragment: Fragment, itemId: Int) {
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

        // Update the selected item in the Bottom Navigation View
        (activity as MainActivity).setSelectedBottomNavItem(itemId)
    }

}