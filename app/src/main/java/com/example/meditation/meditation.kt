package com.example.meditation

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.time.Duration
import java.util.concurrent.TimeUnit

class meditation : AppCompatActivity(){
    private var clickTime: Long = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meditation)
        val elapsedTimeTextView: TextView = findViewById(R.id.elapsedTimeTextView)
        val startButton: Button = findViewById(R.id.start_button)
        val audioButton: ImageButton = findViewById(R.id.audio_button)
        val closeButton: ImageButton = findViewById(R.id.close_button)

        mediaPlayer = MediaPlayer.create(this, R.raw.sample2)
        mediaPlayer.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
                // Handle error here
                return true
            }
        })
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - clickTime
                val HH = TimeUnit.MILLISECONDS.toHours(elapsedTime)
                val MM = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60
                val SS = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
                elapsedTimeTextView.text = String.format("%02d:%02d:%02d", HH, MM, SS)
                handler.postDelayed(this, 1000)
            }
        }

        startButton.setOnClickListener {
            clickTime = System.currentTimeMillis()
            handler.post(runnable)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            startButton.text = "Stop"
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
            showDialog(this@meditation) { // Pass a callback to be executed on "Yes" button click
                handler.removeCallbacks(runnable)

                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                }

                val intent = Intent(this, home::class.java)
                startActivity(intent)
                finish() // Finish the current activity
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}

