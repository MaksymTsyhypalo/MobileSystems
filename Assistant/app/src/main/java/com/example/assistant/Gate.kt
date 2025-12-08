package com.example.assistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Gate : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gate_layout)
        findViewById<Button>(R.id.log_button).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        findViewById<Button>(R.id.reg_button).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}