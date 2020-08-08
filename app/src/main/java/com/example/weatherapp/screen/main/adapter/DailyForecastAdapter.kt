package com.example.weatherapp.screen.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ForecastDailyItemBinding
import com.example.weatherapp.model.daily_forecast.DailyForecasts

class DailyForecastAdapter: RecyclerView.Adapter<DailyForecastAdapter.ForecastViewHolder>() {

    private lateinit var context: Context
    private val forecastList = ArrayList<DailyForecasts>()

    class ForecastViewHolder(private val binding: ForecastDailyItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: DailyForecasts) {
            binding.forecast = forecast
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ForecastDailyItemBinding.inflate(inflater)
        return ForecastViewHolder(binding)
    }

    override fun getItemCount(): Int = forecastList.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bind(forecast)
    }

    fun setNewList(newList: List<DailyForecasts>) {
        forecastList.clear()
        forecastList.addAll(newList)
        notifyDataSetChanged()
    }
}