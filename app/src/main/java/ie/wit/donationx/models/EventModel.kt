package ie.wit.donationx.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(
    val _id: String = "N/A",
    @SerializedName("paymenttype")
    val paymenttype: String = "N/A",
    var message: String = "n/a",
    var amount: Int = 0,
    var upvotes: Int = 0,
    val email: String = "joe@bloggs.com") : Parcelable