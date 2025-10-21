package com.example.assistant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_layout)

        findViewById<Button>(R.id.btn_day).setOnClickListener {
            startActivity(Intent(this, ScrollDayActivity::class.java))
        }

        findViewById<Button>(R.id.btn_week).setOnClickListener {
            startActivity(Intent(this, ScrollWeekActivity::class.java))
        }

        findViewById<Button>(R.id.btn_task).setOnClickListener {
            startActivity(Intent(this, ScrollTaskActivity::class.java))
        }

        findViewById<Button>(R.id.btn_shop).setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }
        findViewById<Button>(R.id.btn_garden).setOnClickListener {
            startActivity(Intent(this, GardenActivity::class.java))
        }

    }
}
