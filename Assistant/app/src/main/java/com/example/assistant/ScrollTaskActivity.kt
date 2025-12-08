package com.example.assistant

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Build
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit

private val items = mutableListOf<String>() // co widzisz na liście
private val ids   = mutableListOf<Int>()    // równoległe ID do usuwania
private var stepcount = 0
public var taskcountdone = 0f;


class ScrollTaskActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var db: DBHelper
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    var currentSteps = 0

    @RequiresApi(Build.VERSION_CODES.O)

    private val Channel_ID = "finish_channel";
    private val notificationID = 1;


    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Finished step task"
            val descriptionText = "You have finished your step task!"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Channel_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendnotification(){
        val builder = NotificationCompat.Builder(this, Channel_ID).setSmallIcon(R.drawable.notification_icon).setContentTitle("Step task finished").setContentText("You have finished your step task").setPriority(
            NotificationCompat.PRIORITY_DEFAULT)
        with (NotificationManagerCompat.from(this)){
            try {
                notify(notificationID, builder.build())
            }
            catch (e: SecurityException){

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_task)
        val ErrorText = findViewById<TextView>(R.id.ErrorView)
        db = DBHelper(this)

        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION), 100)
            }
        }

        if (Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor found!", Toast.LENGTH_LONG).show()
        }

        loadData()


        val taskTitle = intent.getStringExtra("Task_Title")
        val taskDesc = intent.getStringExtra("Task_Description")
        val etext = findViewById<EditText>(R.id.edit1);
        val dtext = findViewById<EditText>(R.id.edittextdesc);

        if (taskTitle != null){
            dtext.setText(taskTitle);}
        if (taskDesc != null){etext.setText(taskDesc);}

        val intent1 = Intent(this, ScrollDayActivity::class.java)
        intent1.putExtra("Task_Title", taskTitle)
        intent1.putExtra("Task_Description", taskDesc)


        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        var mainCheckBoxvar = findViewById<CheckBox>(R.id.checkBox4)
        findViewById<CheckBox>(R.id.checkBox4).setOnClickListener {
            if(mainCheckBoxvar.isChecked() == true){
                taskcountdone += 1;
            }
            else if (taskcountdone == 0f){
                taskcountdone = 0f;
            }
            else if(mainCheckBoxvar.isChecked() == false && taskcountdone > 0){
                taskcountdone -= 1;
            }
        }

        var checkBoxvar = findViewById<CheckBox>(R.id.checkBox)
        findViewById<CheckBox>(R.id.checkBox).setOnClickListener{

            if(checkBoxvar.isChecked() == true){
                try{
                    val usersteps: Int
                    if (taskDesc.isNullOrEmpty()) {
                        ErrorText.setText("Number of steps is missing.")
                        checkBoxvar.isChecked = false
                        return@setOnClickListener
                    }
                    usersteps = taskDesc.toInt()
                    dtext.setText("$currentSteps / $usersteps steps taken")

                    if(usersteps <= currentSteps){
                        mainCheckBoxvar.isChecked = true
                        Toast.makeText(this, "Task finished!", Toast.LENGTH_LONG).show()
                        sendnotification()
                    }
                }
                catch (n: NumberFormatException){
                    ErrorText.setText("No correct number was entered. Try again!")
                    checkBoxvar.isChecked = false
                }
            }
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            Toast.makeText(this@ScrollTaskActivity, "Saved", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ScrollDayActivity::class.java))
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, ScrollDayActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        registerSensor()
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }

    private fun registerSensor() {
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        // update step count
        if (running) {
            totalSteps = event.values[0]
            currentSteps = (totalSteps - previousTotalSteps).toInt()
        }
    }

    override fun onResume() {
        super.onResume()
        running = true
        registerSensor()
    }

    override fun onPause() {
        super.onPause()
        running = false
    }

    private fun resetSteps() {
            previousTotalSteps = totalSteps
            saveData()
            true
        }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        sharedPreferences.edit {
            putFloat("key1", previousTotalSteps)
        }
    }

    // load step count
    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        previousTotalSteps = sharedPreferences.getFloat("key1", 0f)
    }

}
