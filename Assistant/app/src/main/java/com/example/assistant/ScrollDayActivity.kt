package com.example.assistant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class ScrollDayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_day)

        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<Button>(R.id.button5).setOnClickListener{ startActivity(Intent(this, ScrollTaskActivity::class.java)) }
        findViewById<Button>(R.id.button8).setOnClickListener{ startActivity(Intent(this, ScrollTaskActivity::class.java)) }
    }
}
