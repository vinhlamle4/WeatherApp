package com.example.weatherapp.screen.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.forecast.DailyForecasts

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private lateinit var context: Context
    private val forecastList = ArrayList<DailyForecasts>()

    class ForecastViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgDay: ImageView = view.findViewById(R.id.img_day)
        val imgNight: ImageView = view.findViewById(R.id.img_night)
        val tvTemp: TextView = view.findViewById(R.id.tv_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.forecast_item, parent, false)
        return ForecastViewHolder(view)
    }

    override fun getItemCount(): Int = forecastList.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = forecastList[position]

        holder.setViewData(forecast)
    }

    private fun ForecastViewHolder.setViewData(forecast: DailyForecasts) {
        imgDay.setImageResource(getImageResource(forecast.day.icon))
        imgNight.setImageResource(getImageResource(forecast.night.icon))

        val temp = "%s°C - %s°C"
        val tempRange = String.format(temp, forecast.temperature.maximum.value, forecast.temperature.minimum.value)
        tvTemp.text = tempRange
    }

    private fun getImageResource(weatherIcon: Int): Int {
        val iconName = "icon_$weatherIcon"
        return context.resources.getIdentifier("drawable/$iconName", null, context.packageName)
    }

    fun setNewList(newList: List<DailyForecasts>) {
        forecastList.clear()
        forecastList.addAll(newList)
    }
}