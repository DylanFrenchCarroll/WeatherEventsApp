package ie.wit.eventx.ui.event

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.eventx.firebase.FirebaseImageManager
import ie.wit.eventx.models.EventModel

class EventViewModel : ViewModel() {

    private val status = FirebaseImageManager.status

    val observableStatus: LiveData<Boolean>
        get() = status


    fun addEvent(
        firebaseUser: MutableLiveData<FirebaseUser>,
        event: EventModel,
        imgID: String,
        eventImage: Uri
    ) {
        try {
            event.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseImageManager.uploadImageEvent(imgID, eventImage, firebaseUser, event)
        } catch (e: IllegalArgumentException) {

        }

    }

}
