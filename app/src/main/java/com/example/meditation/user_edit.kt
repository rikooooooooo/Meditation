package com.example.meditation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class user_edit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_edit)

        // Initialize views
        val textView2: TextView = findViewById(R.id.rg_email)
        val textView3: TextView = findViewById(R.id.rg_username)
        val textView4: TextView = findViewById(R.id.rg_password)

        val updateButton = findViewById<Button>(R.id.update_button)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        val editText1 = findViewById<EditText>(R.id.rg_username_insert)
        val editText2 = findViewById<EditText>(R.id.rg_email_insert)
        val editText3 = findViewById<EditText>(R.id.rg_password_insert)

        val storedUsername = getStoredUsername()

        // Set click listeners
        updateButton.setOnClickListener {
            val username = editText1.text.toString()
            val email = editText2.text.toString()
            val password = editText3.text.toString()
            val BASE_URL = "https://forprojectk.000webhostapp.com/api-update.php"

            if (!(username.isEmpty() || email.isEmpty() || password.isEmpty())) {

                val stringRequest = object : StringRequest(
                    Request.Method.POST,
                    BASE_URL,
                    Response.Listener { response ->
                        Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
                        Log.d("Server Response", response)

                        // Check the response and perform actions accordingly
                        if (response.contains("Update successful")) {
                            // Update successful, perform any additional actions
                            Toast.makeText(applicationContext, "Update successful", Toast.LENGTH_SHORT).show()
                        } else if (response.contains("User not found")) {
                            // User not found in the database, handle accordingly
                            Toast.makeText(applicationContext, "User not found. Cannot update.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle other responses or errors
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                    }) {

                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["username"] = username
                        params["email"] = email
                        params["password"] = password
                        return params
                    }
                }

                val requestQueue = Volley.newRequestQueue(applicationContext)
                requestQueue.add(stringRequest)

            } else {
                Toast.makeText(applicationContext, "Data cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, user::class.java))
        }
    }
    private fun getStoredUsername(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }
}