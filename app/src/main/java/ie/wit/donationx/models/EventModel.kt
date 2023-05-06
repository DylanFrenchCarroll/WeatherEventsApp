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
    var paymenttype: String = "N/A",
    var amount: Int = 0,
    var message: String = "Homer for President!",
    var upvotes: Int = 0,
    var profilepic: String = "",
    var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "paymenttype" to paymenttype,
            "amount" to amount,
            "message" to message,
            "upvotes" to upvotes,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}

