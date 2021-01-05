package com.example.weatherapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ForecastDailyItemBinding
import com.example.weatherapp.model.daily_forecast.DailyForecasts

//class DailyForecastAdapter: RecyclerView.Adapter<DailyForecastAdapter.ForecastViewHolder>() {
//
//    private lateinit var context: Context
//    private val forecastList = ArrayList<DailyForecasts>()
//
//    class ForecastViewHolder(private val binding: ForecastDailyItemBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(forecast: DailyForecasts) {
//            binding.forecast = forecast
//            binding.executePendingBindings()
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
//        context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val binding = ForecastDailyItemBinding.inflate(inflater)
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
//    fun setNewList(newList: List<DailyForecasts>) {
//        forecastList.clear()
//        forecastList.addAll(newList)
//        notifyDataSetChanged()
//    }
//}

class DailyForecastAdapter : ListAdapter<DailyForecasts, DailyForecastAdapter.ForecastViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<DailyForecasts>() {
            override fun areItemsTheSame(
                oldItem: DailyForecasts,
                newItem: DailyForecasts
            ): Boolean {
                return oldItem.date == newItem.date && oldItem.temperature == newItem.temperature
            }

            override fun areContentsTheSame(
                oldItem: DailyForecasts,
                newItem: DailyForecasts
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ForecastDailyItemBinding.inflate(inflater)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

//    private class DiffCallback: DiffUtil.ItemCallback<DailyForecasts>() {
//        override fun areItemsTheSame(oldItem: DailyForecasts, newItem: DailyForecasts): Boolean {
//            return oldItem.date == newItem.date && oldItem.temperature == newItem.temperature
//        }
//
//        override fun areContentsTheSame(oldItem: DailyForecasts, newItem: DailyForecasts): Boolean {
//            return oldItem == newItem
//        }
//    }

    class ForecastViewHolder(private val binding: ForecastDailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: DailyForecasts) {
            binding.forecast = forecast
            binding.executePendingBindings()
        }
    }
}