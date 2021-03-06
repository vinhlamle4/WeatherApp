package com.example.weatherapp.ui.main.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.main.adapter.DailyForecastAdapter
import com.example.weatherapp.ui.main.adapter.HourForecastAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() {

    private val dailyForecastAdapter by lazy {
        DailyForecastAdapter()
    }
    private val hourForecastAdapter by lazy {
        HourForecastAdapter()
    }

    internal val mainViewModel: MainViewModel by viewModel()
    internal var fetchDataFromAPI = false

    internal lateinit var binding: ActivityMainBinding
    internal lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpToolbar()
        setAppBackground()
        initApp()
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
        showProgressDialog(true)
        mainViewModel.setAppBackGround()
    }

    private fun initApp() {
        getWeatherFromLocal()
        initRecycler()
        initObserver()
    }

    private fun getWeatherFromLocal() {
        mainViewModel.getWeatherInformationFromLocal()
    }

    private fun initRecycler() {
        val dividerVertical =
            DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        dividerVertical.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.divider_view
            )!!
        )

        binding.apply {
            recyclerDailyForecast.run {
                addItemDecoration(dividerVertical)
                adapter = dailyForecastAdapter
            }

            recyclerHourForecast.run {
                adapter = hourForecastAdapter
            }
        }
    }

    private fun initObserver() = mainViewModel.apply {
        location.observe(this@MainActivity, {
            binding.tvCityName.text = it.englishName
            //fetchDataFromAPI = true => search city from searchView fetch data and save to DB
            if (it.Key.isNotBlank() && fetchDataFromAPI) {
                getConditionAPI(it.Key)
                getForecastAPI(it.Key)
                getHourForecastAPI(it.Key)
                fetchDataFromAPI = false
            }
        })

        condition.observe(this@MainActivity, {
            binding.condition = it
        })

        hourForecast.observe(this@MainActivity, {
            hourForecastAdapter.submitList(it.toMutableList())
        })

        dailyForecast.observe(this@MainActivity, {
            dailyForecastAdapter.submitList(it.toMutableList())
            showProgressDialog(false)
        })

        requestFail.observe(this@MainActivity, {
            showProgressDialog(false)
            showSnackBar(binding.frameMain, it)
        })

        bitmapComposer.observe(this@MainActivity, {
            binding.imvAppBg.foreground =
                ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.black_40))
            it.into(binding.imvAppBg)
            showProgressDialog(false)
        })
    }

    private fun showProgressDialog(isShow: Boolean) = binding.includeProgress.frameProgress.apply {
        if (isShow) {
            visibility = View.VISIBLE
            return@apply
        }
        visibility = View.GONE
    }
}