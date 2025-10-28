package com.example.assistant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class ScrollWeekActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_week)

        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<Button>(R.id.day1_edit).setOnClickListener{ startActivity(Intent(this, ScrollDayActivity::class.java)) }
        findViewById<Button>(R.id.day2_edit).setOnClickListener{ startActivity(Intent(this, ScrollDayActivity::class.java)) }
        findViewById<Button>(R.id.day3_edit).setOnClickListener{ startActivity(Intent(this, ScrollDayActivity::class.java)) }
        findViewById<Button>(R.id.day4_edit).setOnClickListener{ startActivity(Intent(this, ScrollDayActivity::class.java)) }
        findViewById<Button>(R.id.day5_edit).setOnClickListener{ startActivity(Intent(this, ScrollDayActivity::class.java)) }
        findViewById<Button>(R.id.day6_edit).setOnClickListener{ startActivity(Intent(this, ScrollDayActivity::class.java)) }
        findViewById<Button>(R.id.day7_edit).setOnClickListener{ startActivity(Intent(this, ScrollDayActivity::class.java)) }

    }
}
