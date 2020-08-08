package com.example.weatherapp.screen.main.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.amitshekhar.DebugDB
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.screen.main.adapter.DailyForecastAdapter
import com.example.weatherapp.screen.main.adapter.HourForecastAdapter
import com.example.weatherapp.screen.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_condition.view.*
import kotlinx.android.synthetic.main.progress_bar.*


class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var dailyForecastAdapter: DailyForecastAdapter
    private lateinit var hourForecastAdapter: HourForecastAdapter
    private var isAPISearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupData()
        getRoomDb()
        setupRecycler()
        setupViewObserver()
        setupViewsAction()
        setDetailsViews()

        DebugDB.getAddressLog()
    }

    private fun setupData() {
        dailyForecastAdapter = DailyForecastAdapter()
        hourForecastAdapter = HourForecastAdapter()
    }

    private fun getRoomDb() {
        mainViewModel.getLocationDb()
    }

    private fun setupRecycler() {
        binding.recyclerDailyForecast.apply {
            val dividerVertical =
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.HORIZONTAL)
            dividerVertical.setDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.divider_view
                )!!
            )
            addItemDecoration(dividerVertical)
            setHasFixedSize(true)
            adapter = dailyForecastAdapter
        }

        binding.recyclerHourForecast.apply {
            setHasFixedSize(true)
            adapter = hourForecastAdapter
        }
    }

    private fun setupViewObserver() {
        mainViewModel.location.observe(this, Observer {
            binding.tvCityName.text = it.englishName
            if (!it.Key.isBlank() && isAPISearch) {
                mainViewModel.getConditionAPI(it.Key)
                mainViewModel.getForecastAPI(it.Key)
                mainViewModel.getHourForecastAPI(it.Key)
                isAPISearch = false
            }
        })

        mainViewModel.condition.observe(this, Observer {
            binding.condition = it
        })

        mainViewModel.hourForecast.observe(this, Observer {
            hourForecastAdapter.setNewList(it)
        })

        mainViewModel.dailyForecast.observe(this, Observer {
            dailyForecastAdapter.setNewList(it)
            frame_progress.visibility = View.GONE
        })

        mainViewModel.requestFail.observe(this, Observer {
            frame_progress.visibility = View.GONE
            showSnackBar(frame_main, it)
        })
    }

    private fun setupViewsAction() {
        edt_city_name.setOnEditorActionListener { v, actionId, _ ->
            var handler = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                isAPISearch = true
                frame_progress.visibility = View.VISIBLE
                mainViewModel.getLocationAPI(v.text.toString())
                // clear focus and close keyboard
                v.text = ""
                v.clearFocus()
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                handler = true
            }
            //mainViewModel.getForecast("4-353981_1_AL")
            handler
        }
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