package ie.wit.donationx.models

import androidx.lifecycle.MutableLiveData
import ie.wit.donationx.api.EventClient
import ie.wit.donationx.api.EventWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object EventManager : EventStore {

    val events = ArrayList<EventModel>()

    override fun findAll(eventsList: MutableLiveData<List<EventModel>>) {

        val call = EventClient.getApi().getall()

        call.enqueue(object : Callback<List<EventModel>> {
            override fun onResponse(call: Call<List<EventModel>>,
                                    response: Response<List<EventModel>>
            ) {
                eventsList.value = response.body() as ArrayList<EventModel>
                Timber.i("Retrofit JSON = ${response.body()}")
            }

            override fun onFailure(call: Call<List<EventModel>>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })
    }


    override fun findById(id:String) : EventModel? {
        val foundEvent: EventModel? = events.find { it._id == id }
        return foundEvent
    }

    override fun create(event: EventModel) {

        val call = EventClient.getApi().post(event)

        call.enqueue(object : Callback<EventWrapper> {
            override fun onResponse(call: Call<EventWrapper>,
                                    response: Response<EventWrapper>
            ) {
                val eventWrapper = response.body()
                if (eventWrapper != null) {
                    Timber.i("Retrofit ${eventWrapper.message}")
                    Timber.i("Retrofit ${eventWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<EventWrapper>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })
    }
    override fun delete(id: String) {
        val call = EventClient.getApi().delete(id)

        call.enqueue(object : Callback<EventWrapper> {
            override fun onResponse(call: Call<EventWrapper>,
                                    response: Response<EventWrapper>
            ) {
                val eventWrapper = response.body()
                if (eventWrapper != null) {
                    Timber.i("Retrofit Delete ${eventWrapper.message}")
                    Timber.i("Retrofit Delete ${eventWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<EventWrapper>, t: Throwable) {
                Timber.i("Retrofit Delete Error : $t.message")
            }
        })
    }



    fun logAll() {
        Timber.v("** Events List **")
        events.forEach { Timber.v("Event ${it}") }
    }
}