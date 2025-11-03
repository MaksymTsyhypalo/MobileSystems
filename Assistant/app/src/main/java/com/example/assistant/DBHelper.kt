package com.example.assistant

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val dayKey: String    // np. "2026-01-01" albo "today"
)

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "tasks.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE tasks(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                day_key TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldV: Int, newV: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    fun addTask(title: String, description: String, dayKey: String): Long {
        val cv = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("day_key", dayKey)
        }
        return writableDatabase.insert("tasks", null, cv)
    }

    fun deleteTask(id: Int): Int =
        writableDatabase.delete("tasks", "id=?", arrayOf(id.toString()))

    fun getTasksForDay(dayKey: String): List<Task> {
        val out = mutableListOf<Task>()
        val c = readableDatabase.rawQuery(
            "SELECT id,title,description,day_key FROM tasks WHERE day_key=? ORDER BY id DESC",
            arrayOf(dayKey)
        )
        c.use {
            while (it.moveToNext()) {
                out += Task(
                    id = it.getInt(0),
                    title = it.getString(1) ?: "",
                    description = it.getString(2) ?: "",
                    dayKey = it.getString(3) ?: ""
                )
            }
        }
        return out
    }
}
