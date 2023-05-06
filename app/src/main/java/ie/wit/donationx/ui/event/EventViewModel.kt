package ie.wit.donationx.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.donationx.firebase.FirebaseDBManager
import ie.wit.donationx.firebase.FirebaseImageManager
import ie.wit.donationx.models.EventModel

class EventViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status


    fun addEvent(firebaseUser: MutableLiveData<FirebaseUser>, event: EventModel) {
        status.value = try {
            event.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,event)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

}
