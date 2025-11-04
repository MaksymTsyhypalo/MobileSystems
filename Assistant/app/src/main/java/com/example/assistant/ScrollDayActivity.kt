package com.example.assistant

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ScrollDayActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var adapter: ArrayAdapter<String>
    private val items = mutableListOf<String>() // co widzisz na liście
    private val ids   = mutableListOf<Int>()    // równoległe ID do usuwania
    private val dayKey = "today"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_day)

        db = DBHelper(this)

        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        val etTitle = findViewById<EditText>(R.id.etTaskTitle)
        val etDesc  = findViewById<EditText>(R.id.etTaskDesc)
        val btnAdd  = findViewById<Button>(R.id.btnAddTask)
        val list    = findViewById<ListView>(R.id.listTasks)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        list.adapter = adapter

        refreshList()

        // Dodawanie
        btnAdd.setOnClickListener {
            val t = etTitle.text.toString().trim()
            val d = etDesc.text.toString().trim()
            if (t.isEmpty()) {
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            db.addTask(t, d, dayKey)
            etTitle.text.clear(); etDesc.text.clear()
            refreshList()
        }

        // Usuwanie (long press)
        list.setOnItemLongClickListener { _, _, position, _ ->
            val id = ids[position]
            db.deleteTask(id)
            Toast.makeText(this, "Deleted task #$id", Toast.LENGTH_SHORT).show()
            refreshList()
            true
        }

        //short tap
        list.setOnItemClickListener { _, _, position, _ ->
            val id = ids[position]



            val senderSTR = items[position]

            val strstart = senderSTR.indexOf(" · ") + 3

            val strend = senderSTR.indexOf(" — ")

            val taskTitle: String = if(strend > strstart){senderSTR.substring(strstart, strend)}
            else {senderSTR.substring(strstart)}

            val taskDesc: String = if(strend > strstart){senderSTR.substring(strstart, strend)}
            else {senderSTR.substring(strstart)}

            Toast.makeText(this, "Editing task #$id", Toast.LENGTH_SHORT).show()

            val intent1 = Intent(this, ScrollTaskActivity::class.java)
            intent1.putExtra("Task_Title", taskTitle)
            intent1.putExtra("Task_Description", taskDesc)

            refreshList()
            startActivity(intent1)
        }
    }

    private fun refreshList() {
        val data = db.getTasksForDay(dayKey)
        items.clear(); ids.clear()
        data.forEach { t ->
            ids += t.id
            items += "${t.id} · ${t.title}" + if (t.description.isNotBlank()) " — ${t.description}" else ""
        }
        adapter.notifyDataSetChanged()
    }
}
