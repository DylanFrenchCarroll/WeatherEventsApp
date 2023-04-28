package ie.wit.donationx.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.donationx.models.EventManager
import ie.wit.donationx.models.EventModel

class EventViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addEvent(event: EventModel) {
        status.value = try {
            EventManager.create(event)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
