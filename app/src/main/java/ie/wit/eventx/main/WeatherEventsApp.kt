package ie.wit.eventx.main

import android.app.Application
import timber.log.Timber

class WeatherEvents : Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Weather Events Application Started")
    }
}