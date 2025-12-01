package com.example.assistant

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

var taskcountother = taskcount

var taskcountdoneother = taskcountdone


class StatisticsActivity : AppCompatActivity() {
    lateinit var barChart: BarChart
    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    lateinit var barEntriesList: ArrayList<BarEntry>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)

        barChart = findViewById(R.id.my_bar_chart)

        setupChartData()

        barDataSet = BarDataSet(barEntriesList, "Task Chart Data")

        barData = BarData(barDataSet)

        barChart.data = barData

        barDataSet.valueTextColor = Color.BLACK

        barDataSet.setColor(Color.GREEN)

        barDataSet.valueTextSize = 14f

        barChart.description.isEnabled = false


    }

    fun setupChartData(){
        barEntriesList = ArrayList()

        barEntriesList.add(BarEntry(1f, taskcountdoneother))

        barEntriesList.add(BarEntry(2f, taskcountother))
    }
}

