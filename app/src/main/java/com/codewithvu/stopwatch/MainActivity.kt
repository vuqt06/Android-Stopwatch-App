package com.codewithvu.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    lateinit var stopWatch: Chronometer
    var running = false
    var offset: Long = 0

    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stopWatch = findViewById<Chronometer>(R.id.stopwatch)

        // Restore the previous state
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(RUNNING_KEY)) {
                stopWatch.base = savedInstanceState.getLong(BASE_KEY)
                stopWatch.start()
            } else setBaseTime()
        }

        // Start the stopwatch if is is not running
        val start_button = findViewById<Button>(R.id.start_button)
        start_button.setOnClickListener {
            if (!running) {
                setBaseTime()
                stopWatch.start()
                running = true
            }
        }

        // Pause the stopwatch if it is running
        val pause_button = findViewById<Button>(R.id.pause_button)
        pause_button.setOnClickListener {
            if (running) {
                saveOffset()
                stopWatch.stop()
                running = false
            }
        }

        // Sets the offset and stopwatch to 0
        val reset_button = findViewById<Button>(R.id.reset_button)
        reset_button.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    override fun onStop() {
        super.onStop()
        if (running) {
            saveOffset()
            stopWatch.stop()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (running) {
            setBaseTime()
            stopWatch.start()
            offset = 0
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, stopWatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    // Update the stopwatch.base time, allowing for any offset
    fun setBaseTime() {
        stopWatch.base = SystemClock.elapsedRealtime() - offset
    }

    // Recored the offset
    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopWatch.base
    }
}