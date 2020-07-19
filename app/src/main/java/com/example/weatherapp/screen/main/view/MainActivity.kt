package com.example.weatherapp.screen.main.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.location.Location
import com.example.weatherapp.screen.main.viewmodel.MainViewModel
import com.example.weatherapp.service.WeatherRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_condition.view.*
import kotlinx.android.synthetic.main.progress_bar.*


class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var location: Location
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupData()
        setupViewObserver()
        setupViewsAction()
        setDetailsViews()
    }

    private fun setupData() {
        mainViewModel.service = WeatherRepository()
    }

    private fun setupViewObserver() {
        mainViewModel.location.observe(this, Observer {
            location = it[0]
            if (!location.Key.isBlank()) {
                mainViewModel.getCondition(location.Key)
            }
        })

        mainViewModel.condition.observe(this, Observer {
            binding.condition = it
            setImageWeatherIcon(it.weatherIcon)
            frame_progress.visibility = View.GONE
        })
    }

    private fun setupViewsAction() {
//        edt_city_name.setOnEditorActionListener { v, actionId, _ ->
//            var handler = false
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                mainViewModel.getLocation(v.text.toString())
//                // clear focus and close keyboard
//                v.clearFocus()
//                val imm: InputMethodManager =
//                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(v.windowToken, 0)
//                handler = true
//            }
//            handler
//        }
        img_location_search.setOnClickListener {
            frame_progress.visibility = View.VISIBLE
            showToast("City Search")
            mainViewModel.getCondition("4-353981_1_AL")
        }
    }

    private fun setImageWeatherIcon(weatherIcon: Int) {
        val iconName = "icon_$weatherIcon"
        val res = resources.getIdentifier("drawable/$iconName", null, packageName)
        img_weather.setImageDrawable(null)
        img_weather.setImageResource(res)
    }

    private fun setDetailsViews() {
        feels_like.img_icon.setImageResource(R.drawable.ic_thermometer)
        feels_like.tv_title.text = getString(R.string.lbl_feels_like)

        wind.img_icon.setImageResource(R.drawable.ic_wind)
        wind.tv_title.text = getString(R.string.lbl_wind)

        humidity.img_icon.setImageResource(R.drawable.ic_humidity)
        humidity.tv_title.text = getString(R.string.lbl_humidity)

        pressure.img_icon.setImageResource(R.drawable.ic_pressure)
        pressure.tv_title.text = getString(R.string.lbl_pressure)

        visibility.img_icon.setImageResource(R.drawable.ic_eye)
        visibility.tv_title.text = getString(R.string.lbl_visibility)

        dew_point.img_icon.setImageResource(R.drawable.ic_dew_point)
        dew_point.tv_title.text = getString(R.string.dew_point)
    }
}



