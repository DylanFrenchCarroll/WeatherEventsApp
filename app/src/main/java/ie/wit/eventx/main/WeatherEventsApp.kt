package ie.wit.eventx.main

import android.app.Application
import ie.wit.eventx.models.EventStore
import timber.log.Timber

class WeatherEvents : Application() {

    lateinit var eventsStore: EventStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        eventsStore = EventMemStore()
        Timber.i("Weather Events Application Started")
    }
}