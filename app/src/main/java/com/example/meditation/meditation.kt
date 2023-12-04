package com.example.meditation

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.time.Duration
import java.util.concurrent.TimeUnit
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.lang.ref.WeakReference


class meditation : AppCompatActivity(){
    private lateinit var runnable: Runnable
    private var clickTime: Long = 0
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var countDownTimer: CountDownTimer
    var totalElapsedTime: Long = 0
    private var formattedTime: String = ""

    private val handler = Handler(Looper.getMainLooper())
    private val weakReference = WeakReference(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meditation)
        val elapsedTimeTextView: TextView = findViewById(R.id.elapsedTimeTextView)
        val startButton: Button = findViewById(R.id.start_button)
        val audioButton: ImageButton = findViewById(R.id.audio_button)
        val closeButton: ImageButton = findViewById(R.id.close_button)

//        val sharedPref = getSharedPreferences("MyTime", Context.MODE_PRIVATE)
//        totalElapsedTime = sharedPref.getLong("totalElapsedTime", 0)

        var isPlaying = false
        runnable = Runnable {
            val handler = Handler(Looper.getMainLooper())
            handler.removeCallbacks(runnable)
        }


        mediaPlayer = MediaPlayer.create(this, R.raw.sample2)
        mediaPlayer.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
                // Handle error here
                return true
            }
        })
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                totalElapsedTime += 1000
                val HH = TimeUnit.MILLISECONDS.toHours(totalElapsedTime)
                val MM = TimeUnit.MILLISECONDS.toMinutes(totalElapsedTime) % 60
                val SS = TimeUnit.MILLISECONDS.toSeconds(totalElapsedTime) % 60
                formattedTime = String.format("%02d:%02d:%02d", HH, MM, SS)
                elapsedTimeTextView.text = formattedTime
            }

            override fun onFinish() {
                // Not needed for a repeating timer
            }
        }

        startButton.setOnClickListener {
            if (!isPlaying) {
                clickTime = System.currentTimeMillis()
                countDownTimer.start()
                mediaPlayer.isLooping = true
                mediaPlayer.start()
                isPlaying = true
                startButton.text = "Pause"
            } else {
                countDownTimer.cancel()
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
                isPlaying = false
                startButton.text = "Continue"
            }
        }


        audioButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                audioButton?.setImageResource(R.drawable.baseline_music_off_24)
            } else {
                mediaPlayer.start()
                audioButton?.setImageResource(R.drawable.baseline_music_note_24)
            }
        }

        closeButton.setOnClickListener {
            val activity = weakReference.get()
            activity?.let {
                showDialog(activity) {
                    handler.removeCallbacks(runnable)
                    val storedUsername = getStoredUsername()
                    val BASE_URL = "https://forprojectk.000webhostapp.com/api-meditation.php"

                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                    }
                    if (formattedTime.isNotEmpty() && formattedTime != "00:00:00") {
                        val stringRequest = object : StringRequest(
                            Request.Method.POST,
                            BASE_URL,
                            Response.Listener { response ->
                                // Handle the response from the server
                                Log.d("Server Response", response)
                                Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
                                // Check the response and perform actions accordingly
                                if (response.contains("Meditation data inserted successfully")) {
                                    // Update successful, perform any additional actions
                                    Toast.makeText(
                                        applicationContext,
                                        "Update successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (response.contains("User not found")) {
                                    // User not found in the database, handle accordingly
                                    Toast.makeText(
                                        applicationContext,
                                        "User not found. Cannot update.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Handle other responses or errors
                                }
                                // Finish the current activity
                                val intent = Intent(activity, home::class.java)
                                activity.startActivity(intent)
                            },
                            Response.ErrorListener { error ->
                                // Handle errors
                                Log.e("Error", error.toString())
                                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }) {

                            override fun getParams(): Map<String, String> {
                                val params: MutableMap<String, String> = HashMap()
                                params["username"] =
                                    storedUsername // Assuming you have storedUsername defined somewhere
                                params["lama_meditasi"] = formattedTime
                                // Add any other parameters you need to send
                                Log.d("Parameters", "Username: $storedUsername")
                                Log.d("Parameters", "Total Elapsed Time: $formattedTime")
                                return params
                            }
                        }
                        val requestQueue = Volley.newRequestQueue(applicationContext)
                        requestQueue.add(stringRequest)
                    } else {
                        val intent = Intent(activity, home::class.java)
                        activity.startActivity(intent)
                    }
                }
            }
        }


    }
        fun showDialog(activity: AppCompatActivity, onYesClicked: () -> Unit) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

            builder.setMessage("Do you want to stop meditating ?").setCancelable(true)
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                onYesClicked.invoke() // Execute the callback on "Yes" button click
                dialog.dismiss()
            })

            builder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            val alert = builder.create()
            alert.setTitle("Are you sure")
            alert.show()
        }
    private fun getStoredUsername(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "") ?: ""
    }
//    override fun onDestroy() {
//        super.onDestroy()
//
//        mediaPlayer.release()
//    }

}



