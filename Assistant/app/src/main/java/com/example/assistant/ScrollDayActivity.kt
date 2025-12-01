package com.example.assistant

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

var taskcount = 0f;

class ScrollDayActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var adapter: ArrayAdapter<String>
    private val items = mutableListOf<String>() // co widzisz na liście
    private val ids   = mutableListOf<Int>()    // równoległe ID do usuwania
    private val taskTitles = mutableListOf<String>()
    private val taskDescriptions = mutableListOf<String>()
    private val dayKey = "today"
    private val Channel_ID = "day_channel";
    private val notificationID = 1;

    val etTitle = findViewById<EditText>(R.id.etTaskTitle)
    val etDesc  = findViewById<EditText>(R.id.etTaskDesc)
    val btnAdd  = findViewById<Button>(R.id.btnAddTask)
    val list    = findViewById<ListView>(R.id.listTasks)

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Task created"
            val descriptionText = "You have created new task! Task name: " + etTitle.toString();
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Channel_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun sendnotification(){
        val builder = NotificationCompat.Builder(this, Channel_ID).setSmallIcon(R.drawable.notification_icon).setContentTitle("Step task finished").setContentText("You have finished your step task").setPriority(
            NotificationCompat.PRIORITY_DEFAULT)
        with (NotificationManagerCompat.from(this)){
            notify(notificationID,builder.build())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_day)
        createNotificationChannel()

        db = DBHelper(this)

        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

            //val etTitle = findViewById<EditText>(R.id.etTaskTitle)
        //val etDesc  = findViewById<EditText>(R.id.etTaskDesc)
        //val btnAdd  = findViewById<Button>(R.id.btnAddTask)
        //val list    = findViewById<ListView>(R.id.listTasks)

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
            taskcount += 1;
        }

        // Usuwanie (long press)
        list.setOnItemLongClickListener { _, _, position, _ ->
            val id = ids[position]
            db.deleteTask(id)
            if(taskcount > 0){taskcount -= 1;}
            Toast.makeText(this, "Deleted task #$id", Toast.LENGTH_SHORT).show()
            refreshList()
            true
        }

        list.setOnItemClickListener { _, _, position, _ ->
            val id = ids[position]

            val taskTitle: String = taskTitles[position]
            val taskDesc: String = taskDescriptions[position]

            Toast.makeText(this, "Editing task #$id", Toast.LENGTH_SHORT).show()
            sendnotification()

            val intent1 = Intent(this, ScrollTaskActivity::class.java)
            intent1.putExtra("Task_Title", taskTitle)
            intent1.putExtra("Task_Description", taskDesc)

            startActivity(intent1)
        }
    }

    private fun refreshList() {
        val data = db.getTasksForDay(dayKey)
        items.clear(); ids.clear();
        taskTitles.clear()
        taskDescriptions.clear()
        data.forEach { t ->
            ids += t.id
            items += "${t.id} · ${t.title}" + if (t.description.isNotBlank()) " — ${t.description}" else ""
            taskTitles += t.title
            taskDescriptions += t.description
        }
        adapter.notifyDataSetChanged()
    }
}
