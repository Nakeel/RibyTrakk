package com.we2dx.ribytrakks.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import com.we2dx.ribytrakks.R

class SplashScreenActivity : AppCompatActivity() {
    private var splashTimer: CountDownTimer? = null
    private val minute = 5L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //getting the window and making it to use the full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue_semi)
        }
    }

    override fun onResume() {
        super.onResume()
        splashTimer = object : CountDownTimer(minute * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val mainIntent = Intent(applicationContext, MainActivity::class.java)
                startActivity(mainIntent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }.start()
    }
    override fun onStop() {
        super.onStop()
        cancelTimer()
    }
    private fun cancelTimer() {
        if (splashTimer != null)
            splashTimer!!.cancel()
    }
}


