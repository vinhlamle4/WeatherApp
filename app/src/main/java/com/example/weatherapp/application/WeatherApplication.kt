package com.example.weatherapp.application

import android.app.Application
import com.example.weatherapp.di.roomModule
import com.example.weatherapp.di.viewModelModule
import com.example.weatherapp.di.repositoryModule
import com.example.weatherapp.di.retrofitModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
@FlowPreview
class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoIn()
    }

    private fun initKoIn() {
        startKoin {
            androidLogger()
            androidContext(this@WeatherApplication)
            modules(listOf(roomModule, retrofitModule, repositoryModule, viewModelModule))
        }
    }
}