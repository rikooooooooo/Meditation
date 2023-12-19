package com.example.meditation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class user_edit : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit)

        // Initialize views

        val textView2: TextView = findViewById(R.id.rg_email)
        val textView3: TextView = findViewById(R.id.rg_username)
        val textView4: TextView = findViewById(R.id.rg_password)

        val imageView: ImageView = findViewById(R.id.user_img)

        val updateButton = findViewById<Button>(R.id.update_button)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        val editText1 = findViewById<EditText>(R.id.rg_username_insert)
        val editText2 = findViewById<EditText>(R.id.rg_email_insert)
        val editText3 = findViewById<EditText>(R.id.rg_password_insert)

        val sharedPreferences = getSharedPreferences("ImagePreference", Context.MODE_PRIVATE)
        val storedUsername = getStoredUsername()
        //Set Image
        val BASE_URL = "https://forprojectk.000webhostapp.com/api-user_image.php"
        val url = "$BASE_URL"

        if (!storedUsername.isEmpty()) {

            val requestQueue = Volley.newRequestQueue(applicationContext)

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
                            val userImage = jsonResponse.getString("user_image")
                            val imageView: ImageView = findViewById(R.id.user_img)

                            // Get the resource ID of the image
                            val imageResource = try {
                                resources.getIdentifier(userImage, "drawable", packageName)
                            } catch (e: Exception) {
                                // Handle the case where the resource is not found
                                e.printStackTrace()
                                // You can provide a default image resource here or show an error image
                                R.drawable.user
                            }

                            // Set the image resource
                            imageView.setImageResource(imageResource)

                            Toast.makeText(applicationContext, "Data received successfully", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(applicationContext, "Failed to receive data", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
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
            Toast.makeText(applicationContext, "Username is empty", Toast.LENGTH_SHORT).show()
        }
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
                            val intent = Intent(this, login::class.java)
                            startActivity(intent)
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
                        params["username"] = storedUsername
                        params["newUsername"] = username
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
            finish()
        }
    }
    private fun getStoredUsername(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }


}