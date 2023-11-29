package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.meditation.model.SignInBody
import com.example.meditation.retrofit.ApiInterface
import com.example.meditation.retrofit.RetrofitInstance

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class login : AppCompatActivity(){

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



        loginButton.setOnClickListener {
            val username = usernameEditText.getText().toString()
            val password = passwordEditText.getText().toString()
            signin(username, password)
        }
        signupButton.setOnClickListener {
            openNewPage2()
        }

    }
    private fun signin(username: String, password: String){
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val signInInfo = SignInBody(username, password)
        retIn.signin(signInInfo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@login,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@login, "Login success!", Toast.LENGTH_SHORT).show()
                    openNewPage()
                } else {
                    Toast.makeText(this@login, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun openNewPage2() {
        val intent = Intent(this, register::class.java)
        ContextCompat.startActivity(this, intent, null)
    }

    private fun openNewPage() {
        // Add logic to navigate to the new page/activity
        val intent = Intent(this, MainActivity::class.java)
        ContextCompat.startActivity(this, intent, null)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@login, message, Toast.LENGTH_SHORT).show()
        }
    }
}





