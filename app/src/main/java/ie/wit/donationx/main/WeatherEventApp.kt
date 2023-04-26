package ie.wit.donationx.main

import android.app.Application
import ie.wit.donationx.models.EventMemStore
import ie.wit.donationx.models.EventStore
import timber.log.Timber

class WeatherEventApp : Application() {

    lateinit var eventsStore: EventStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        eventsStore = EventMemStore()
        Timber.i("Weather Event Application Started")
    }
}