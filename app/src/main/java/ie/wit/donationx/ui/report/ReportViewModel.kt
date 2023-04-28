package ie.wit.donationx.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.donationx.models.EventManager
import ie.wit.donationx.models.EventModel

class ReportViewModel : ViewModel() {

    private val eventsList = MutableLiveData<List<EventModel>>()

    val observableEventsList: LiveData<List<EventModel>>
        get() = eventsList

    init {
        load()
    }

    fun load() {
        eventsList.value = EventManager.findAll()
    }
}
