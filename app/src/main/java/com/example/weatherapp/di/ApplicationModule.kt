package com.example.weatherapp.di

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.database.WeatherDatabase
import com.example.weatherapp.repository.IWeatherRepository
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.ui.main.view.MainViewModel
import com.example.weatherapp.service.IWeatherService
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
        WeatherDatabase.create(androidContext())
    }
}

val repositoryModule = module {
    single<IWeatherRepository> {
        WeatherRepository(androidApplication(), get(), get())
    }
}

val retrofitModule = module {

    fun initInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    fun initClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    fun initGSonConverter(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    fun initRetrofit(gSonConvert: GsonConverterFactory, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(gSonConvert)
            .client(client)
            .build()
    }

    fun createService(retrofit: Retrofit): IWeatherService {
        return retrofit.create(IWeatherService::class.java)
    }

    single {
        initInterceptor()
    }

    single {
        initClient(get())
    }

    single {
        initGSonConverter()
    }

    single {
        initRetrofit(get(), get())
    }

    single {
        createService(get())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel {
        MainViewModel(androidApplication(), get())
    }
}