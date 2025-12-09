package com.example.assistant

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

var taskcountother = taskcount

var taskcountdoneother = taskcountdone


class StatisticsActivity : AppCompatActivity() {
    lateinit var pieChart: PieChart
    lateinit var pieData: PieData
    lateinit var pieDataSet: PieDataSet
    lateinit var pieEntriesList: ArrayList<PieEntry>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics)

        pieChart = findViewById(R.id.my_pie_chart)

        setupChartData()

        pieDataSet = PieDataSet(pieEntriesList, "Task Chart Data")

        pieData = PieData(pieDataSet)

        pieChart.data = pieData

        pieDataSet.valueTextColor = Color.BLACK

        pieDataSet.setColor(Color.GREEN)

        pieDataSet.valueTextSize = 14f

        pieChart.description.isEnabled = false


    }

    fun setupChartData(){
        pieEntriesList = ArrayList()

        pieEntriesList.add(PieEntry(1f, taskcountdoneother))

        pieEntriesList.add(PieEntry(2f, taskcountother))
    }
}

