package com.example.assistant

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max
import kotlin.math.min

class GardenActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

        // Create a string for the ImageView label.
        val IMAGEVIEW_TAG = "icon bitmap"
        val iconBitmap: Bitmap? = intent.getParcelableExtra("Bitmap", Bitmap::class.java)
        val imageView = ImageView(this).apply {
            // Set the bitmap for the ImageView from an icon bitmap defined elsewhere.
            setImageBitmap(iconBitmap)
            tag = IMAGEVIEW_TAG
            setOnLongClickListener { v ->
                // Create a new ClipData. This is done in two steps to provide
                // clarity. The convenience method ClipData.newPlainText() can
                // create a plain text ClipData in one step.

                // Create a new ClipData.Item from the ImageView object's tag.
                val item = ClipData.Item(v.tag as? CharSequence)

                // Create a new ClipData using the tag as a label, the plain text
                // MIME type, and the already-created item. This creates a new
                // ClipDescription object within the ClipData and sets its MIME type
                // to "text/plain".
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    item)

                // Instantiate the drag shadow builder. We use this imageView object
                // to create the default builder.
                val myShadow = View.DragShadowBuilder(this)

                // Start the drag.
                v.startDragAndDrop(dragData,  // The data to be dragged.
                    myShadow,  // The drag shadow builder.
                    null,      // No need to use local data.
                    0          // Flags. Not currently used, set to 0.
                )

                v.visibility = View.VISIBLE
                // Indicate that the long-click is handled.
                true


            }
        }

        val dragListener = View.OnDragListener{ view, event ->
            val receiverView = view as FrameLayout
            when(event.action){
                DragEvent.ACTION_DROP -> {
                    val dropX = event.x;
                    val dropY = event.y;

                    val offset = size/2;
                    var finalX = (dropX - offset).toInt()
                    var finalY = (dropY - offset).toInt()

                    val maxX = max(0,receiverView.width - size)
                    val maxY = max(0,receiverView.height - size)

                    finalX = (min(max(finalX,0), maxX) / grid) * grid;
                    finalY = (min(max(finalY,0), maxY) / grid) * grid;
                    store.addPlacedItem(GardenItem(type = type, x = finalX, y = finalY))

                    renderPlaced(layer, store)
                    moving.visibility = View.GONE

                    findViewById<LinearLayout>(R.id.placeControls).visibility = View.GONE
                    Toast.makeText(this, "Placed: $type" + " at $finalX, $finalY", Toast.LENGTH_SHORT).show()
                true
                }
                else -> true
                }
            }
        layer.setOnDragListener(dragListener)
        moving.setOnLongClickListener { v ->
            val data = ClipData.newPlainText("type", type)
            val shadow = View.DragShadowBuilder(v)
            v.startDragAndDrop(data, shadow, null, 0)

            v.visibility = View.INVISIBLE
            true
        }
        }


        // Sterowanie przyciskami (skok o 1 kratkę)
            /*findViewById<Button>(R.id.btnLeft).setOnClickListener { x -= grid; applyPos() }
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
        }*/
    }

    private fun drawableForType(type: String): Int {
        return when (type) {
            "house" -> R.drawable.house
            else -> R.drawable.house
        }
    }
