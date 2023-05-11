package ie.wit.eventx.ui.event

import android.net.Uri
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import ie.wit.eventx.firebase.FirebaseImageManager
import ie.wit.eventx.models.EventModel

class EventViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status


    fun addEvent(
        firebaseUser: MutableLiveData<FirebaseUser>,
        event: EventModel,
        imgID: String,
        eventImage: Uri,
        viewLifecycleOwner: LifecycleOwner
    ) {
        try {
            event.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseImageManager.uploadImageEvent(imgID, eventImage, firebaseUser, event)
            FirebaseImageManager.observableFirebaseStatus.observe(
                viewLifecycleOwner,
                Observer { fireBaseStatus ->
                    fireBaseStatus?.let {
                        status.value = true
                    }
                })

        } catch (e: IllegalArgumentException) {
            status.value = false
        }

    }

}
