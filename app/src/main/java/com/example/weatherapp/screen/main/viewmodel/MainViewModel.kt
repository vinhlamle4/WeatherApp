package com.example.weatherapp.screen.main.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.SUCCESS_CODE
import com.example.weatherapp.model.forecast.Forecast
import com.example.weatherapp.model.location.Location
import com.example.weatherapp.request.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var service: WeatherService

    private val _location = MutableLiveData<List<Location>>()
    val location: LiveData<List<Location>> get() = _location

    private val _forecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast> get() = _forecast

    fun setupRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(WeatherService::class.java)
    }

    fun getLocation(location: String) {
        viewModelScope.launch {
            callLocationAPI(location)
        }
    }

    private suspend fun callLocationAPI(location: String) {
        withContext(Dispatchers.Default) {
            val call = service.getLocation(BuildConfig.API_KEY, location)

            call.enqueue(object : Callback<List<Location>> {
                override fun onFailure(
                    call: Call<List<Location>>, t: Throwable
                ) {
                    // Request Fail
                    Log.e("Response Error --->", "${t.message}")
                }

                override fun onResponse(
                    call: Call<List<Location>>, response: Response<List<Location>>
                ) {
                    // Request Success
                    if (response.code() == SUCCESS_CODE) {
                        val locationResponse = response.body()

                        val locationList = ArrayList<Location>()
                        locationResponse?.forEach {
                            locationList.add(it)
                            Log.i("Response Success --->", "$it")
                        }
                        _location.postValue(locationList.toList())
                    }
                }
            })
        }
    }

    fun getForecast(locationKey: String) {
        viewModelScope.launch {
            callForecastApI(locationKey)
        }
    }

    private suspend fun callForecastApI(locationKey: String) {
        withContext(Dispatchers.Default) {
            val call = service.getForecast(
                locationKey, BuildConfig.API_KEY,
                details = true,
                metric = true
            )

            call.enqueue(object : Callback<Forecast> {
                override fun onFailure(call: Call<Forecast>, t: Throwable) {
                    Log.e("Response Error --->", "${t.message}")
                }

                override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                    if (response.code() == SUCCESS_CODE) {
                        response.body()?.let {
                            val forecastInfo = Forecast(it.headline, it.dailyForecasts)
                            _forecast.postValue(forecastInfo)
                        }
                    }
                }
            })
        }
    }
}