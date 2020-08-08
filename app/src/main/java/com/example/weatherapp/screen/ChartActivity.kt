package com.example.weatherapp.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        line_chart.apply {
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawAxisLine(false)
            axisLeft.setDrawGridLines(false)

            axisRight.setDrawLabels(false)
            axisRight.setDrawAxisLine(false)
            axisRight.setDrawGridLines(false)

            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)

            legend.isEnabled = false
            description.text = ""

            setScaleEnabled(false)
            setTouchEnabled(false)
            extraTopOffset = 50f
        }


        val entries: MutableList<Entry> = ArrayList()
        entries.add(Entry(0f, 1f))
        entries.add(Entry(1f, 2f))
        entries.add(Entry(2f, 3f))
        entries.add(Entry(3f, 3f))
        entries.add(Entry(4f, 5f))
        entries.add(Entry(5f, 6f))
        entries.add(Entry(6f, 7f))
        entries.add(Entry(7f, 4f))
        entries.add(Entry(8f, 5f))
        entries.add(Entry(9f, 3f))
        entries.add(Entry(10f, 2f))
        entries.add(Entry(11f, 2f))
        entries.add(Entry(12f, 6f))

        val xAxis = line_chart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        val hour = arrayOf("1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm", "12pm")
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(hour)
        xAxis.textSize = 13F

        val dataSet = LineDataSet(entries, "Customized values")
        dataSet.setDrawFilled(true)
        dataSet.valueTextSize = 12f
        dataSet.circleHoleColor = ContextCompat.getColor(this, R.color.colorAccent)
        dataSet.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        val data = LineData(dataSet)
        line_chart.data = data
        line_chart.invalidate()
    }
}