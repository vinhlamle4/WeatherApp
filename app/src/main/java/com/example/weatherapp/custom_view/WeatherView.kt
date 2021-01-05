package com.example.weatherapp.custom_view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.weatherapp.R

class WeatherView : RelativeLayout {
    lateinit var imgIcon: ImageView
    lateinit var tvTitle: TextView
    lateinit var tvValue: TextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val view = LayoutInflater.from(context).inflate(R.layout.weather_view, this, true)
        imgIcon = view.findViewById(R.id.img_icon)
        tvTitle = view.findViewById(R.id.tv_title)
        tvValue = view.findViewById(R.id.tv_value)

        val arr = context.obtainStyledAttributes(attrs, R.styleable.WeatherView)
        setAttr(arr)
        arr.recycle()
    }

    private fun setAttr(arr: TypedArray) {
        val title = arr.getString(R.styleable.WeatherView_title)
        tvTitle.text = title

        val icon = arr.getResourceId(R.styleable.WeatherView_icon, -1)
        imgIcon.setImageResource(icon)
    }

    fun setValue(text: String?) {
        if(text!= null) {
            tvValue.text = text
        }
    }
}