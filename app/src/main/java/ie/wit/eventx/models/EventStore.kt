package ie.wit.eventx.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser


interface EventStore {
    fun findAll(eventsList: MutableLiveData<List<EventModel>>)
    fun findAll(userid: String, eventsList: MutableLiveData<List<EventModel>>)
    fun findById(userid: String, eventid: String, event: MutableLiveData<EventModel>)
    fun findById(eventid: String, event: MutableLiveData<EventModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, event: EventModel)
    fun delete(userid: String, eventid: String)
    fun update(userid: String, eventid: String, event: EventModel)
}



