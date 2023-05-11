package ie.wit.eventx.firebase

import android.annotation.SuppressLint


import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.ktx.messaging

import timber.log.Timber


object FirebaseDBService : FirebaseMessagingService() {


    @SuppressLint("StaticFieldLeak")
    var service: FirebaseMessaging = FirebaseMessaging.getInstance()

    fun getToken() {
        service.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.i("Fetching FCM registration token failed" + task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            val msg = token
            Timber.i("FCM Token: " + msg)
        })
    }

    fun subToTopic() {
        Firebase.messaging.subscribeToTopic("weatherEvents").addOnCompleteListener { task ->
            var msg = "Subscribed to weatherEvents topic"
            if (!task.isSuccessful) {
                msg = "Subscribe failed"
            }
            Timber.i("$msg")
        }
    }

    //    /**
//     * Called if the FCM registration token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the
//     * FCM registration token is initially generated so this is where you would retrieve the token.
//     */
    override fun onNewToken(token: String) {
        Timber.i("New FCM Token: $token")
    }


}
