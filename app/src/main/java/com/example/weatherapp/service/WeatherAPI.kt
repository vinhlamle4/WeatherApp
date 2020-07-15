package com.example.weatherapp.service

class WeatherAPI: Retrofit() {
    private var weatherService: WeatherService? = null

    companion object {
        val getInstance = WeatherAPI()
    }

    fun newInstance(): WeatherService {
        if (weatherService == null) {
            weatherService = createService(WeatherService::class.java)
        }
        return weatherService as WeatherService
    }


}