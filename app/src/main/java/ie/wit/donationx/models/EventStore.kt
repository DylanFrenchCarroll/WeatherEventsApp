package ie.wit.donationx.models

import androidx.lifecycle.MutableLiveData

interface EventStore {
    fun findAll(eventsList: MutableLiveData<List<EventModel>>)
    fun findById(id: String) : EventModel?
    fun create(event: EventModel)
    fun delete(id: String)
}
