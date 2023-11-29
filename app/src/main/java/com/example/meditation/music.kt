package com.example.meditation

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import java.util.Locale
import kotlinx.coroutines.delay



class music : Fragment()  {
    private var mediaPlayer: MediaPlayer? = null
    private var seekBar: SeekBar? = null

    lateinit var runnable: Runnable
    private var handler = Handler()

    private val fastForwardTime = 10000 // 10 seconds in milliseconds
    private val rewindTime = -10000 // -10 seconds in milliseconds

    private lateinit var elapsedTimeTextView: TextView
    private lateinit var totalTimeTextView: TextView

    private lateinit var elapsedTimer: CountDownTimer



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        val playBtn = view.findViewById<ImageButton>(R.id.play_btn)
        val rewindButton = view.findViewById<ImageButton>(R.id.rewind_btn)
        val forwardButton = view.findViewById<ImageButton>(R.id.forward_btn)

        val backButton = view.findViewById<ImageButton>(R.id.back_button)

        elapsedTimeTextView = view.findViewById(R.id.elapsedTimeTextView)
        totalTimeTextView = view.findViewById(R.id.totalTimeTextView)
        seekBar = view.findViewById<SeekBar>(R.id.seekbar)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sample)

        mediaPlayer?.setOnPreparedListener {
            seekBar?.max = mediaPlayer!!.duration
        }

        backButton.setOnClickListener {
            replaceFragment(home(), R.id.home)
        }

        seekBar?.progress = 0

        playBtn.setOnClickListener {
            togglePlayPause()
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer?.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing needed here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing needed here
            }
        })

        // Fast Forward Button
        forwardButton.setOnClickListener {
            fastForward()
        }

        // Rewind Button
        rewindButton.setOnClickListener {
            fastRewind()
        }

        runnable = Runnable {
            seekBar?.progress = mediaPlayer?.currentPosition ?: 0
            handler.postDelayed(runnable, 1000)
        }

        handler.postDelayed(runnable, 1000)

        val localSeekBar = seekBar
        mediaPlayer?.setOnCompletionListener {
            val playButton = view.findViewById<ImageButton>(R.id.play_btn)
            playButton.setImageResource(R.drawable.baseline_play_arrow_24)
            localSeekBar?.progress = 0
        }

        elapsedTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateElapsedTime()
            }

            override fun onFinish() {
                // Handle timer finish if needed
            }
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

    private fun fastForward() {
        mediaPlayer?.let {
            val currentPosition = it.currentPosition
            val newPosition = currentPosition + fastForwardTime
            if (newPosition <= it.duration) {
                it.seekTo(newPosition)
                seekBar?.progress = newPosition
            }
        }
    }

    private fun fastRewind() {
        mediaPlayer?.let {
            val currentPosition = it.currentPosition
            val newPosition = currentPosition + rewindTime
            if (newPosition >= 0) {
                it.seekTo(newPosition)
                seekBar?.progress = newPosition
            } else {
                it.seekTo(0)
                seekBar?.progress = 0
            }
        }
    }

    private fun updateElapsedTime() {
        mediaPlayer?.let {
            val elapsedTime = it.currentPosition / 1000
            elapsedTimeTextView.text = formatTime(elapsedTime)
        }
    }

    private fun updateTotalTime() {
        mediaPlayer?.let {
            val totalTime = it.duration / 1000
            totalTimeTextView.text = formatTime(totalTime)
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
    }

    private fun startTimers() {
        elapsedTimer.start()
        updateTotalTime()
    }

    private fun stopTimers() {
        elapsedTimer.cancel()
    }

    public fun togglePlayPause() {
        val playBtn = view?.findViewById<ImageButton>(R.id.play_btn)
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            stopTimers()
            playBtn?.setImageResource(R.drawable.baseline_play_arrow_24)
        } else {
            mediaPlayer?.start()
            startTimers()
            playBtn?.setImageResource(R.drawable.baseline_pause_24)
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        releaseMediaPlayer()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }
}

