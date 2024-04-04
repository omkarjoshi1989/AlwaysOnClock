package com.example.alwaysonclock

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val clockTextView = findViewById<TextView>(R.id.clock_text)
        val battery = findViewById<TextView>(R.id.battery)
        val myClock = MyClock(clockTextView, battery)
        myClock.start(this)

    }
}

class MyClock(private val textView: TextView, private val battery:TextView) {

    private val handler = Handler()
    private var running = false

    fun start(mainActivity: MainActivity) {
        running = true
        refreshTime(mainActivity)
    }

    fun stop() {
        running = false
    }

    private fun refreshTime(context:Context) {
        val currentDate = Date()
        val onlyTime = SimpleDateFormat("HH:mm")
        val formattedTime = onlyTime.format(currentDate)
        val onlyDate = SimpleDateFormat("EEE, dd MMMM")
        val formattedDate = onlyDate.format(currentDate)
        println("Current time: $formattedTime")
        //Log.d("=====","====="+formattedTime)
        textView.text = formattedDate + "\n" + formattedTime

        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

// For a single call (without broadcast receiver)
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, intentFilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = (level / scale.toDouble()) * 100
        Log.d("=====","====="+batteryPct)
        battery.text = ""+batteryPct


        if (running) {
            handler.postDelayed({ refreshTime(context) }, 1000)
        }
    }
}