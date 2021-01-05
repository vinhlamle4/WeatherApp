package com.example.weatherapp.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.TitleViewBinding

class TitleView: LinearLayout {
    private lateinit var tvTitle: TextView
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val binding = TitleViewBinding.inflate(LayoutInflater.from(context), this)
        tvTitle = binding.tvTitle

        val arr = context.obtainStyledAttributes(attrs, R.styleable.TitleView)
        val label = arr.getString(R.styleable.TitleView_label)
        setText(label)
        arr.recycle()
    }

    private fun setText(text: String?) {
        tvTitle.text = text
    }
}