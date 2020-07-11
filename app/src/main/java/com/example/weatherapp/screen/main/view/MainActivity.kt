package com.example.weatherapp.screen.main.view

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.model.location.Location
import com.example.weatherapp.screen.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity: BaseActivity()  {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupData()
        setupViewObserver()
        setupViewsAction()
    }

    private fun setupData() {
        mainViewModel.setupRetrofit()
    }

    private fun setupViewObserver() {
        mainViewModel.location.observe(this, Observer {
            location = it[0]
            if (!location.Key.isBlank()) {
                mainViewModel.getForecast(location.Key)
            }
        })
        
        mainViewModel.forecast.observe(this, Observer {

        })
    }

    private fun setupViewsAction() {
        edt_city_name.setOnEditorActionListener { v, actionId, _ ->
            var handler = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mainViewModel.getLocation(v.text.toString())
                // clear focus and close keyboard
                v.clearFocus()
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                handler = true
            }
            handler
        }
    }
}



