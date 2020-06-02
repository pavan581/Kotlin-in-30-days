package com.sai_ram.timer


import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var START_MILLI_SECONDS = 60_000L
    val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)

    lateinit var countdown_timer: CountDownTimer
    var isRunning: Boolean = false;
    var time_in_milli_seconds = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        start_button.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                val time = time_edit_text.text.toString()
                if (time_in_milli_seconds >= 0) {
                    time_in_milli_seconds = time.toLong() * 60_000L
                    startTimer(time_in_milli_seconds)
                }
                else{
                    val toast = Toast.makeText(applicationContext,"Enter some time...", LENGTH_SHORT)
                    toast.show()
                }
            }
        }

        reset_button.setOnClickListener {
            resetTimer()
        }


    }

    private fun pauseTimer() {
        start_button.text = "Start"
        countdown_timer.cancel()
        isRunning = false
        reset_button.visibility = View.VISIBLE
    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1_000) {
            override fun onFinish() {
                beep(1_000)
                reset_button.visibility = View.INVISIBLE
                start_button.visibility = View.VISIBLE
                start_button.text = "Start"
                time_in_milli_seconds = 0
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer.start()

        isRunning = true
        start_button.text = "Pause"
        reset_button.visibility = View.INVISIBLE

    }

    private fun resetTimer() {
        time_in_milli_seconds = START_MILLI_SECONDS
        updateTextUI()
        reset_button.visibility = View.INVISIBLE
    }

    private fun updateTextUI() {
        val minute = (time_in_milli_seconds / 1_000) / 60
        val seconds = (time_in_milli_seconds / 1_000) % 60

        timer.text = "$minute:$seconds"

    }

    fun beep(duration: Int) {
        toneG.startTone(ToneGenerator.TONE_DTMF_S, duration)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            toneG.release()
        }, (duration + 50).toLong())
    }
}