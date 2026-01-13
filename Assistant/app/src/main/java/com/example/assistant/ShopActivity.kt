package com.example.assistant

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ShopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shop)

        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<Button>(R.id.vb).setOnClickListener {
            startActivity(Intent(this, GardenActivity::class.java))
        }

        val store = GameStore(this)

        // UI coins (żółty button na górze)
        val coinsBtn = findViewById<Button>(R.id.button9)
        fun refreshCoins() {
            coinsBtn.text = "${store.getCoins()} TASKCOINS"
        }
        refreshCoins()

        // BUY house
        findViewById<Button>(R.id.button15).setOnClickListener {
            val cost = 15
            val coins = store.getCoins()

            if (coins < cost) {
                Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // odejmij coins
            store.setCoins(coins - cost)
            refreshCoins()

            Toast.makeText(this, "Bought: house! Place it in garden.", Toast.LENGTH_SHORT).show()

            val House = findViewById<ImageView>(R.id.House)
            // zamiast od razu zapisywać pozycję -> przejdź do ogrodu w trybie ustawiania
            val i = Intent(this, GardenActivity::class.java)
            i.putExtra("PENDING_TYPE", "house")

            startActivity(i)

        }

        //PLACEHOLDERS 1,2,3 FOR NEW ELEMENTS:
        findViewById<Button>(R.id.button11).setOnClickListener {
            val cost = 10
            val coins = store.getCoins()

            if (coins < cost) {
                Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // odejmij coins
            store.setCoins(coins - cost)
            refreshCoins()

            Toast.makeText(this, "Bought: rose! Place it in garden.", Toast.LENGTH_SHORT).show()

            val Rose = findViewById<ImageView>(R.id.Rose)
            // zamiast od razu zapisywać pozycję -> przejdź do ogrodu w trybie ustawiania
            val i = Intent(this, GardenActivity::class.java)
            i.putExtra("PENDING_TYPE", "rose")

            startActivity(i)

        }
        findViewById<Button>(R.id.button12).setOnClickListener {
            val cost = 15
            val coins = store.getCoins()

            if (coins < cost) {
                Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // odejmij coins
            store.setCoins(coins - cost)
            refreshCoins()

            Toast.makeText(this, "Bought: statue! Place it in garden.", Toast.LENGTH_SHORT).show()

            val Statue = findViewById<ImageView>(R.id.Statue)
            // zamiast od razu zapisywać pozycję -> przejdź do ogrodu w trybie ustawiania
            val i = Intent(this, GardenActivity::class.java)
            i.putExtra("PENDING_TYPE", "statue")

            startActivity(i)

        }
        findViewById<Button>(R.id.button13).setOnClickListener {
            val cost = 15
            val coins = store.getCoins()

            if (coins < cost) {
                Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // odejmij coins
            store.setCoins(coins - cost)
            refreshCoins()

            Toast.makeText(this, "Bought: fountain! Place it in garden.", Toast.LENGTH_SHORT).show()

            val Fountain = findViewById<ImageView>(R.id.Fountain)
            // zamiast od razu zapisywać pozycję -> przejdź do ogrodu w trybie ustawiania
            val i = Intent(this, GardenActivity::class.java)
            i.putExtra("PENDING_TYPE", "fountain")

            startActivity(i)

        }
    }
}
