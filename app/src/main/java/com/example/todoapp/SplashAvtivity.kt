package com.example.todoapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import java.util.*
import kotlin.concurrent.schedule

class SplashAvtivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.StatusBarColor)

        var intent = Intent(this, MainActivity::class.java)
        Timer().schedule(1000){
            startActivity(intent)
        }
    }

}