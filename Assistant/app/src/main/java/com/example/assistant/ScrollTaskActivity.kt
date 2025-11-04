package com.example.assistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import android.widget.Toast;
import kotlin.text.indexOf
import kotlin.text.substring

private val items = mutableListOf<String>() // co widzisz na liście
private val ids   = mutableListOf<Int>()    // równoległe ID do usuwania

class ScrollTaskActivity : AppCompatActivity() {
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        db = DBHelper(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_task)

        val taskTitle = intent.getStringExtra("Task_Title")
        val taskDesc = intent.getStringExtra("Task_Description")

        /*val filed = File(filesDir, "descriptions.json");
        if(!filed.exists()){
            filed.createNewFile();
        }
        val file = File(filesDir, "data.json");
        if(!file.exists()){
            file.createNewFile();
        }*/
        val etext = findViewById<EditText>(R.id.edit1);
        val dtext = findViewById<EditText>(R.id.edittextdesc);

        //var Ostringd: String = filed.readText(Charsets.UTF_8);
        //var Ostringx: String = file.readText(Charsets.UTF_8);
        if (taskTitle != null){
        etext.setText(taskTitle);}
        if (taskDesc != null){dtext.setText(taskDesc);}

        val intent1 = Intent(this, ScrollDayActivity::class.java)
        intent1.putExtra("Task_Title", taskTitle)
        intent1.putExtra("Task_Description", taskDesc)



        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<CheckBox>(R.id.checkBox).setOnClickListener { }
        //var button = findViewById<Button>(R.id.button2);
        findViewById<Button>(R.id.button2).setOnClickListener {

            /*var Stringd: String = dtext.getText().toString();
            val Stringx: String = etext.getText().toString();

            filed.writeText(Stringd + "\n", Charsets.UTF_8);
            file.writeText(Stringx + "\n", Charsets.UTF_8);*/
            Toast.makeText(this@ScrollTaskActivity, "Saved", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ScrollDayActivity::class.java))
        }

    }
}
