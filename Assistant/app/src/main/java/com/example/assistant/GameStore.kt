package com.example.assistant

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class GameStore(context: Context) {
    private val prefs = context.getSharedPreferences("game_store", Context.MODE_PRIVATE)

    fun getCoins(): Int = prefs.getInt("coins", 9999) // na start “dużo”, jak chciałeś

    fun setCoins(v: Int) {
        prefs.edit().putInt("coins", v).apply()
    }

    fun addPlacedItem(item: GardenItem) {
        val arr = JSONArray(prefs.getString("placed_items", "[]"))
        val obj = JSONObject().apply {
            put("type", item.type)
            put("x", item.x)
            put("y", item.y)
        }
        arr.put(obj)
        prefs.edit().putString("placed_items", arr.toString()).apply()
    }

    fun getPlacedItems(): List<GardenItem> {
        val arr = JSONArray(prefs.getString("placed_items", "[]"))
        val out = mutableListOf<GardenItem>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            out += GardenItem(
                type = o.getString("type"),
                x = o.getInt("x"),
                y = o.getInt("y")
            )
        }
        return out
    }
}
