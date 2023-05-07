package ie.wit.donationx.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class EventModel(
    var uid: String? = "",
    var eventtype: String = "Crash",
    var message: String = "Homer for President!",
    var profilepic: String = "",
    var eventimg: String = "",
    var email: String? = "joe@bloggs.com",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "eventtype" to eventtype,
            "message" to message,
            "profilepic" to profilepic,
            "eventimg" to eventimg,
            "email" to email,
            "latitude" to latitude,
            "longitude" to longitude
        )
    }
}

