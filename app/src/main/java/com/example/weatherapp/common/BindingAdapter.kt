package com.example.weatherapp.common

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setWeatherIcon")
fun setWeatherIcon(view: ImageView, weatherIcon: Int) {
    val iconName = "icon_$weatherIcon"
    val res = view.context.resources.getIdentifier("drawable/$iconName", null, view.context.packageName)
    view.setImageDrawable(null)
    view.setImageResource(res)
}

@BindingAdapter("app:setTemperature")
fun setTemperature(view: TextView, temp: Double) {
    val text = "$temp°C"
    view.text = text
}