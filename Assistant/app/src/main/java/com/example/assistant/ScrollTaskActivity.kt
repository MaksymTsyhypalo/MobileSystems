package com.example.assistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import android.widget.Toast;

class ScrollTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_task)
        val filed = File(filesDir, "descriptions.json");
        if(!filed.exists()){
            filed.createNewFile();
        }
        val file = File(filesDir, "data.json");
        if(!file.exists()){
            file.createNewFile();
        }
        var etext = findViewById<EditText>(R.id.edit1);
        var dtext = findViewById<EditText>(R.id.edittextdesc);

        var Ostringd: String = filed.readText(Charsets.UTF_8);
        var Ostringx: String = file.readText(Charsets.UTF_8);
        etext.clearComposingText();
        etext.append(Ostringx);
        dtext.append(Ostringd);


        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<CheckBox>(R.id.checkBox).setOnClickListener { }
        var button = findViewById<Button>(R.id.button2);
        findViewById<Button>(R.id.button2).setOnClickListener {
            var Stringd: String = dtext.getText().toString();
            val Stringx: String = etext.getText().toString();

            filed.writeText(Stringd + "\n", Charsets.UTF_8);
            file.writeText(Stringx + "\n", Charsets.UTF_8);
            Toast.makeText(this@ScrollTaskActivity, "Saved", Toast.LENGTH_SHORT).show()
        }

    }
}
