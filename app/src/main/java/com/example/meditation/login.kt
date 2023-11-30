package com.example.meditation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val imageView: ImageView = findViewById(R.id.welcome_img)
        val textLogin: TextView = findViewById(R.id.logintext)
        val textUsername: TextView = findViewById(R.id.tx_username)
        val textPassword: TextView = findViewById(R.id.tx_password)
        val textExtra: TextView = findViewById(R.id.welcome_extra)

        val usernameEditText: EditText = findViewById(R.id.username)
        val passwordEditText: EditText = findViewById(R.id.password)

        val loginButton: Button = findViewById(R.id.login_button)
        val signupButton: Button = findViewById(R.id.signup_button)
        signupButton.setOnClickListener {
            openNewPage2()
        }
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            saveUsername(username)
            val BASE_URL = "https://forprojectk.000webhostapp.com/api-login.php"
            val url = "$BASE_URL?username=$username&password=$password"

            if (!(username.isEmpty() || password.isEmpty())) {

                val requestQueue = Volley.newRequestQueue(applicationContext)

                val stringRequest = StringRequest(
                    Request.Method.GET,
                    url,
                    Response.Listener { response ->
                        Log.d("Server Response", response)
                        if (response == "Selamat Datang") {
                            Toast.makeText(applicationContext, "Login Berhasil", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Login Gagal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                )
                requestQueue.add(stringRequest)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Password Atau Username Salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



        private fun openNewPage2() {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
        }

        private fun saveUsername(username: String) {
            val sharedPreferences: SharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("username", username)
            editor.apply()
        }

        private fun showToast(message: String) {
            runOnUiThread {
                Toast.makeText(this@login, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

