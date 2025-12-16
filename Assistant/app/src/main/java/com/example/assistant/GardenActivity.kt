package com.example.assistant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max
import kotlin.math.min

class GardenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.garden)

        findViewById<Button>(R.id.btn_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<Button>(R.id.shopbutton).setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        val layer = findViewById<FrameLayout>(R.id.gardenLayer)
        val store = GameStore(this)

        // 1) Render już postawionych itemów
        renderPlaced(layer, store)

        // 2) Jeśli przyszliśmy ze sklepu z "pending" (np. house)
        val pendingType = intent.getStringExtra("PENDING_TYPE")
        if (pendingType != null) {
            startPlacement(layer, store, pendingType)
        }
    }

    private fun renderPlaced(layer: FrameLayout, store: GameStore) {
        val placed = store.getPlacedItems()
        for (it in placed) {
            val iv = ImageView(this)
            iv.setImageResource(drawableForType(it.type))

            val size = 180
            val lp = FrameLayout.LayoutParams(size, size)
            lp.leftMargin = it.x
            lp.topMargin = it.y
            iv.layoutParams = lp

            layer.addView(iv)
        }
    }

    private fun startPlacement(layer: FrameLayout, store: GameStore, type: String) {
        val controls = findViewById<LinearLayout>(R.id.placeControls)
        controls.visibility = View.VISIBLE

        // Ruchomy podgląd obiektu (jeszcze nie zapisany)
        val moving = ImageView(this)
        moving.setImageResource(drawableForType(type))

        val size = 220
        var x = 60
        var y = 120

        // "szachownica" — skok co kratkę
        val grid = 60  // px (zmień np. 40/50/80 jak chcesz większe/mniejsze kratki)

        val lp = FrameLayout.LayoutParams(size, size)
        lp.leftMargin = x
        lp.topMargin = y
        moving.layoutParams = lp
        layer.addView(moving)

        fun snapToGrid(value: Int, maxValue: Int): Int {
            val snapped = (value / grid) * grid
            val maxSnapped = (maxValue / grid) * grid
            return min(snapped, maxSnapped)
        }

        fun applyPos() {
            val maxX = max(0, layer.width - size)
            val maxY = max(0, layer.height - size)

            // 1) clamp do ekranu
            x = min(max(x, 0), maxX)
            y = min(max(y, 0), maxY)

            // 2) snap do siatki (jak szachownica)
            x = snapToGrid(x, maxX)
            y = snapToGrid(y, maxY)

            (moving.layoutParams as FrameLayout.LayoutParams).apply {
                leftMargin = x
                topMargin = y
            }
            moving.requestLayout()
        }

        // po narysowaniu warstwy dopiero mamy width/height -> wtedy clamp/snap ma sens
        layer.post {
            // na starcie też dociągnij do siatki
            applyPos()
        }

        // Sterowanie przyciskami (skok o 1 kratkę)
        findViewById<Button>(R.id.btnLeft).setOnClickListener { x -= grid; applyPos() }
        findViewById<Button>(R.id.btnRight).setOnClickListener { x += grid; applyPos() }
        findViewById<Button>(R.id.btnUp).setOnClickListener { y -= grid; applyPos() }
        findViewById<Button>(R.id.btnDown).setOnClickListener { y += grid; applyPos() }

        // Zatwierdzenie ustawienia
        findViewById<Button>(R.id.btnPlace).setOnClickListener {
            store.addPlacedItem(GardenItem(type = type, x = x, y = y))
            Toast.makeText(this, "Placed: $type", Toast.LENGTH_SHORT).show()

            controls.visibility = View.GONE
            // żeby nie odpalać placement mode ponownie po back/rotacji
            intent.removeExtra("PENDING_TYPE")
        }
    }

    private fun drawableForType(type: String): Int {
        return when (type) {
            "house" -> R.drawable.house
            else -> R.drawable.house
        }
    }
}
