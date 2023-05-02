package ie.wit.donationx.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.donationx.models.EventManager
import ie.wit.donationx.models.EventModel
import timber.log.Timber

class ReportViewModel : ViewModel() {

    private val eventsList = MutableLiveData<List<EventModel>>()

    val observableEventsList: LiveData<List<EventModel>>
        get() = eventsList

    init {
        load()
    }

    fun load() {
        try {
            EventManager.findAll(eventsList)
            Timber.i("Retrofit Success : $eventsList.value")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }

    fun delete(id: String) {
        try {
            EventManager.delete(id)
            Timber.i("Retrofit Delete Success")
        }
        catch (e: java.lang.Exception) {
            Timber.i("Retrofit Delete Error : $e.message")
        }
    }
}