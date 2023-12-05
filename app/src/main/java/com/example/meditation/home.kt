package com.example.meditation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar


public class home : Fragment() {
    private lateinit var fragmentContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }
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

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        var greeting: String

        val storedUsername = getStoredUsername()
        if (hour >= 0 && hour < 12) {
            greeting = "Good Morning"
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon"
        } else {
            greeting = "Good Night"
        }

        if (homeText2 != null) {
            homeText2.text = "$greeting, $storedUsername!"
        }

        val BASE_URL = "https://forprojectk.000webhostapp.com/api-home.php"
        val url = "$BASE_URL"

        if (!storedUsername.isEmpty()) {

            val requestQueue = Volley.newRequestQueue(fragmentContext)

            val stringRequest = object : StringRequest(
                Method.POST,
                url,
                Response.Listener { response ->
                    Log.d("Server Response", response)
                    try {
                        // Parse JSON response
                        val jsonResponse = JSONObject(response)

                        // Check if the response status is 'success'
                        if (jsonResponse.getString("status") == "success") {
                            val jumlahMeditasi = jsonResponse.getInt("jumlah_meditasi")
                            val lamaMeditasi = jsonResponse.getString("lama_meditasi")
                            val historyMeditasi = jsonResponse.getString("history_meditasi")

                            // Use the received data as needed
                            val homeText4 = view?.findViewById<TextView>(R.id.home_text4)
                            homeText4?.text = "\n$jumlahMeditasi times meditated\n\n$lamaMeditasi minutes meditated"

                            val homeText5 = view?.findViewById<TextView>(R.id.home_text5)
                            homeText5?.text = historyMeditasi

                            Toast.makeText(requireContext(), "Data received successfully", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(requireContext(), "Failed to receive data", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
                }) {

                // Override the getParams() method to send parameters in the request body
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = storedUsername
                    // You can add more parameters if needed

                    return params
                }
            }

            requestQueue.add(stringRequest)
        } else {
            Toast.makeText(requireContext(), "Username is empty", Toast.LENGTH_SHORT).show()
        }

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

    private fun getStoredUsername(): String {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("Name", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }

}