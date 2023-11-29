package com.example.meditation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.meditation.model.Users
import com.example.meditation.retrofit.ApiInterface
import com.example.meditation.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

            val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()
            )

            signup(0, email, username, password, 1, currentTime, currentTime)
        }
        loginButton.setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }
    }
    private fun signup(id: Int, username: String, email: String,
                       password: String, level: Int, created_at: String, updated_at: String){
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val registerInfo = Users(id,username,email,password, level, created_at,  updated_at)

        retIn.registerUser(registerInfo).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@register,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    Toast.makeText(this@register, "Registration success!", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@register, started::class.java))
                }
                else{
                    Toast.makeText(this@register, "Registration failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}