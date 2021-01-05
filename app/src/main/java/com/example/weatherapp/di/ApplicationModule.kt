package com.example.weatherapp.di

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.database.WeatherDatabase
import com.example.weatherapp.repository.IWeatherRepository
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.ui.main.view.MainViewModel
import com.example.weatherapp.service.WeatherService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val roomModule = module {
    single {
        WeatherDatabase.getDatabase(androidContext())
    }
}

val repositoryModule = module {
    single<IWeatherRepository> {
        WeatherRepository(androidApplication(), get(), get())
    }
}

val retrofitModule = module {
    single {
        BuildConfig.API_URL
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        val builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(get<HttpLoggingInterceptor>())
        }
        builder.build()
    }

    single {
        GsonConverterFactory.create()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<String>())
            .addConverterFactory(get())
            .client(get())
            .build()
    }

    single<WeatherService> {
        get<Retrofit>().create(WeatherService::class.java)
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel {
        MainViewModel(androidApplication(), get())
    }
}