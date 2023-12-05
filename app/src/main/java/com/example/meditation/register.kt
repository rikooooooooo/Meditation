package com.example.meditation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        val registerButton = findViewById<Button>(R.id.register_button)
        val loginButton = findViewById<Button>(R.id.login_button)
        val backButton = findViewById<ImageButton>(R.id.back_button)

        val textView: TextView = findViewById(R.id.registertext)
        val textView2: TextView = findViewById(R.id.rg_email)
        val textView3: TextView = findViewById(R.id.rg_username)
        val textView4: TextView = findViewById(R.id.rg_password)
        val textView5: TextView = findViewById(R.id.welcome_extra)

        val editText1 = findViewById<EditText>(R.id.rg_username_insert)
        val editText2 = findViewById<EditText>(R.id.rg_email_insert)
        val editText3 = findViewById<EditText>(R.id.rg_password_insert)
        registerButton.setOnClickListener {
            val username = editText1.text.toString()
            val email = editText2.text.toString()
            val password = editText3.text.toString()
            saveUsername(username)
            val BASE_URL = "https://forprojectk.000webhostapp.com/api-register.php"
            val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()
            )
            if (!(username.isEmpty() || email.isEmpty() || password.isEmpty())) {

                val stringRequest = object : StringRequest(Method.POST,BASE_URL,
                    Response.Listener { response ->
                        Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
                        Log.d("Server Response", response)
                        if (response.contains("Registration successful")) {
                            // Start the 'started' activity
                            startActivity(Intent(applicationContext, started::class.java))
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
        loginButton.setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }
    }
    private fun saveUsername(username: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }
    }
