package ie.wit.donationx.api

import ie.wit.donationx.models.EventModel
import retrofit2.Call
import retrofit2.http.*


interface EventService {
    @GET("/donations")
    fun getall(): Call<List<EventModel>>

    @GET("/donations/{id}")
    fun get(@Path("id") id: String): Call<EventModel>

    @DELETE("/donations/{id}")
    fun delete(@Path("id") id: String): Call<EventWrapper>

    @POST("/donations")
    fun post(@Body event: EventModel): Call<EventWrapper>

    @PUT("/donations/{id}")
    fun put(@Path("id") id: String,
            @Body event: EventModel
    ): Call<EventWrapper>
}


