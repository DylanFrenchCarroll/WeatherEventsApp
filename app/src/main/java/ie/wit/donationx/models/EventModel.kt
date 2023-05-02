package ie.wit.donationx.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class EventModel(var _id: String = "N/A",
                         val paymenttype: String = "N/A",
                         val amount: Int = 0,
                         val message: String = "n/a") : Parcelable
