package ie.wit.donationx.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.donationx.models.EventModel
import ie.wit.donationx.models.EventStore
import timber.log.Timber

object FirebaseDBManager : EventStore {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun findAll(eventsList: MutableLiveData<List<EventModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, eventsList: MutableLiveData<List<EventModel>>) {

        database.child("user-donations").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<EventModel>()
                    val children = snapshot.children
                    children.forEach {
                        val event = it.getValue(EventModel::class.java)
                        localList.add(event!!)
                    }
                    database.child("user-donations").child(userid)
                        .removeEventListener(this)

                    eventsList.value = localList
                }
            })
    }


    override fun findById(userid: String, eventid: String, event: MutableLiveData<EventModel>) {

        database.child("user-donations").child(userid)
            .child(eventid).get().addOnSuccessListener {
                event.value = it.getValue(EventModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }


    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, event: EventModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("donations").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        event.uid = key
        val eventValues = event.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/donations/$key"] = eventValues
            childAdd["/user-donations/$uid/$key"] = eventValues

        database.updateChildren(childAdd)
    }


    override fun delete(userid: String, eventid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/donations/$eventid"] = null
        childDelete["/user-donations/$userid/$eventid"] = null

        database.updateChildren(childDelete)
    }


    override fun update(userid: String, eventid: String, event: EventModel) {

        val eventValues = event.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["donations/$eventid"] = eventValues
        childUpdate["user-donations/$userid/$eventid"] = eventValues

        database.updateChildren(childUpdate)
    }

}
