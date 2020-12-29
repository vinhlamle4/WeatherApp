package com.example.weatherapp.screen.main.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.amitshekhar.DebugDB
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.screen.main.adapter.DailyForecastAdapter
import com.example.weatherapp.screen.main.adapter.HourForecastAdapter
import com.example.weatherapp.screen.main.viewmodel.MainViewModel


class MainActivity : BaseActivity() {

    private lateinit var dailyForecastAdapter: DailyForecastAdapter
    private lateinit var hourForecastAdapter: HourForecastAdapter

    internal val mainViewModel: MainViewModel by viewModels()
    internal var isAPISearch = false // to showing progress bar
    internal lateinit var binding: ActivityMainBinding
    internal lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpToolbar()
        setAppBackground()
        setupData()
        getRoomDb()
        setupRecycler()
        setupViewObserver()
        setDetailsViews()
        DebugDB.getAddressLog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let { handleToolBarMenu(it) }
        return true
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
            return
        }
        super.onBackPressed()
    }

    private fun setAppBackground() {
        binding.includeProgress.frameProgress.visibility = View.VISIBLE
        mainViewModel.setAppBackGround()
    }

    private fun setupData() {
        dailyForecastAdapter = DailyForecastAdapter()
        hourForecastAdapter = HourForecastAdapter()
    }

    private fun getRoomDb() {
        mainViewModel.getWeatherInformationFromLocal()
    }

    private fun setupRecycler() {
        val dividerVertical =
            DividerItemDecoration(this@MainActivity, DividerItemDecoration.HORIZONTAL)
        dividerVertical.setDrawable(
            ContextCompat.getDrawable(
                this@MainActivity,
                R.drawable.divider_view
            )!!
        )

        binding.recyclerDailyForecast.apply {
            addItemDecoration(dividerVertical)
            adapter = dailyForecastAdapter
        }

        binding.recyclerHourForecast.apply {
            adapter = hourForecastAdapter
        }
    }

    private fun setupViewObserver() {
        mainViewModel.location.observe(this, {
            binding.tvCityName.text = it.englishName
            if (it.Key.isNotBlank() && isAPISearch) {
                mainViewModel.getConditionAPI(it.Key)
                mainViewModel.getForecastAPI(it.Key)
                mainViewModel.getHourForecastAPI(it.Key)
                isAPISearch = false
            }
        })

        mainViewModel.condition.observe(this, {
            binding.condition = it
        })

        mainViewModel.hourForecast.observe(this, {
            hourForecastAdapter.submitList(it.toMutableList())
        })

        mainViewModel.dailyForecast.observe(this, {
            dailyForecastAdapter.submitList(it.toMutableList())
            binding.includeProgress.frameProgress.visibility = View.GONE
        })

        mainViewModel.requestFail.observe(this, {
            binding.includeProgress.frameProgress.visibility = View.GONE
            showSnackBar(binding.frameMain, it)
        })

        mainViewModel.bitmap.observe(this, {
            binding.imvAppBg.setImageBitmap(it)
            binding.includeProgress.frameProgress.visibility = View.GONE
        })
    }

    private fun setDetailsViews() {
        binding.apply {
            feelsLike.imgIcon.setImageResource(R.drawable.ic_thermometer)
            feelsLike.tvTitle.text = getString(R.string.lbl_feels_like)

            wind.imgIcon.setImageResource(R.drawable.ic_wind)
            wind.tvTitle.text = getString(R.string.lbl_wind)

            humidity.imgIcon.setImageResource(R.drawable.ic_humidity)
            humidity.tvTitle.text = getString(R.string.lbl_humidity)

            pressure.imgIcon.setImageResource(R.drawable.ic_pressure)
            pressure.tvTitle.text = getString(R.string.lbl_pressure)

            visibility.imgIcon.setImageResource(R.drawable.ic_eye)
            visibility.tvTitle.text = getString(R.string.lbl_visibility)

            dewPoint.imgIcon.setImageResource(R.drawable.ic_dew_point)
            dewPoint.tvTitle.text = getString(R.string.dew_point)
        }
    }
}