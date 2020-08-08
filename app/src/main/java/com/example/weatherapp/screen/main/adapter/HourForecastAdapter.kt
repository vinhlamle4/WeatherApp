package com.example.weatherapp.screen.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForecastHourItemBinding
import com.example.weatherapp.model.hour_forecast.HourForecast

class HourForecastAdapter : RecyclerView.Adapter<HourForecastAdapter.ForecastViewHolder>() {

    private lateinit var context: Context
    private val forecastList = ArrayList<HourForecast>()

    class ForecastViewHolder(private val binding: ForecastHourItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: HourForecast) {
            binding.hourForecast = forecast
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ForecastHourItemBinding.inflate(inflater)
        return ForecastViewHolder(binding)
    }

    override fun getItemCount(): Int = forecastList.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bind(forecast)
    }

    fun setNewList(newList: ArrayList<HourForecast>) {
        forecastList.clear()
        forecastList.addAll(newList)
        notifyDataSetChanged()
    }
}