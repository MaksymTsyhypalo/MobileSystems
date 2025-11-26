package com.example.assistant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.github.mikephil.charting.charts.BarChart

var taskcount = ScrollDayActivity().taskcount

var taskcountdoneother = taskcountdone


class StatisticsActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)

        // 1. Find the chart view by its ID
        barChart = findViewById(R.id.my_bar_chart)

        setupChartData()
    }

    fun setupChartData(){

    }
}

