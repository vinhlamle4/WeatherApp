package com.example.weatherapp.custom_view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.WeatherViewBinding

class WeatherView : RelativeLayout {

    private lateinit var binding: WeatherViewBinding

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        binding = WeatherViewBinding.inflate(LayoutInflater.from(context), this)

        val arr = context.obtainStyledAttributes(attrs, R.styleable.WeatherView)
        setAttr(arr)
        arr.recycle()
    }

    private fun setAttr(arr: TypedArray) {
        val title = arr.getString(R.styleable.WeatherView_title)
        binding.tvTitle.text = title

        val icon = arr.getResourceId(R.styleable.WeatherView_icon, -1)
        binding.imgIcon.setImageResource(icon)
    }

    fun setValue(text: String?) {
        if (text != null) {
            binding.tvValue.text = text
        }
    }
}