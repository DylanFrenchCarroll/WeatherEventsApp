package ie.wit.donationx.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object EventManager : EventStore {

    val events = ArrayList<EventModel>()

    override fun findAll(): List<EventModel> {
        return events
    }

    override fun findById(id:Long) : EventModel? {
        val foundEvent: EventModel? = events.find { it.id == id }
        return foundEvent
    }

    override fun create(event: EventModel) {
        event.id = getId()
        events.add(event)
        logAll()
    }

    fun logAll() {
        Timber.v("** Events List **")
        events.forEach { Timber.v("Event ${it}") }
    }
}