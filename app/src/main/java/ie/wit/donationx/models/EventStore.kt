package ie.wit.donationx.models

interface EventStore {
    fun findAll() : List<EventModel>
    fun findById(id: Long) : EventModel?
    fun create(event: EventModel)
}