package com.example.weatherapp.screen.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ForecastHourItemBinding
import com.example.weatherapp.model.hour_forecast.HourForecast

//class HourForecastAdapter : RecyclerView.Adapter<HourForecastAdapter.ForecastViewHolder>() {
//
//    private lateinit var context: Context
//    private val forecastList = ArrayList<HourForecast>()
//
//    class ForecastViewHolder(private val binding: ForecastHourItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(forecast: HourForecast) {
//            binding.hourForecast = forecast
//            binding.executePendingBindings()
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
//        context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val binding = ForecastHourItemBinding.inflate(inflater)
//        return ForecastViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = forecastList.size
//
//    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
//        val forecast = forecastList[position]
//        holder.bind(forecast)
//    }
//
//    fun setNewList(newList: ArrayList<HourForecast>) {
//        forecastList.clear()
//        forecastList.addAll(newList)
//        notifyDataSetChanged()
//    }
//}

class HourForecastAdapter : ListAdapter<HourForecast, HourForecastAdapter.ForecastViewHolder>(
    UTIL_CALLBACK
) {
    companion object {
        val UTIL_CALLBACK = object : DiffUtil.ItemCallback<HourForecast>() {
            override fun areItemsTheSame(oldItem: HourForecast, newItem: HourForecast): Boolean {
                return oldItem.epochDateTime == newItem.epochDateTime
            }

            override fun areContentsTheSame(oldItem: HourForecast, newItem: HourForecast): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ForecastViewHolder(private val binding: ForecastHourItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: HourForecast) {
            binding.hourForecast = forecast
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ForecastHourItemBinding.inflate(inflater)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}