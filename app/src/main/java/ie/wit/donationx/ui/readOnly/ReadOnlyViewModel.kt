package ie.wit.donationx.ui.readOnly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.donationx.firebase.FirebaseDBManager
import ie.wit.donationx.models.EventModel
import timber.log.Timber



class ReadOnlyViewModel : ViewModel() {
    private val event = MutableLiveData<EventModel>()

    var observableEvent: LiveData<EventModel>
        get() = event
        set(value) {event.value = value.value}


    fun getEvent( eventid: String) {
        try {
            FirebaseDBManager.findById( eventid, event)
            Timber.i("Detail getEvent() Success : ${
                event.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getEvent() Error : $e.message")
        }
    }

}
