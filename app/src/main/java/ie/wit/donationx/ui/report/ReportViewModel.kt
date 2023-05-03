package ie.wit.donationx.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.donationx.models.EventManager
import ie.wit.donationx.models.EventModel
import timber.log.Timber

class ReportViewModel : ViewModel() {

    private val eventsList =
        MutableLiveData<List<EventModel>>()

    val observableEventsList: LiveData<List<EventModel>>
        get() = eventsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init { load() }

    fun load() {
        try {
            EventManager.findAll(liveFirebaseUser.value?.email!!, eventsList)
            Timber.i("Report Load Success : ${eventsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(email: String, id: String) {
        try {
            EventManager.delete(email,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}
