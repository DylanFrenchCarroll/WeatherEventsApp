package ie.wit.donationx.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.donationx.models.EventManager
import ie.wit.donationx.models.EventModel

class EventDetailViewModel : ViewModel() {
    private val event = MutableLiveData<EventModel>()

    val observableEvent: LiveData<EventModel>
        get() = event

    fun getEvent(id: Long) {
        event.value = EventManager.findById(id)
    }
}