package ie.wit.eventx.firebase;

import android.annotation.SuppressLint
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast

import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.ktx.messaging
import ie.wit.eventx.R
import ie.wit.eventx.models.EventModel;
import timber.log.Timber;


object FirebaseDBService : FirebaseMessagingService() {



    @SuppressLint("StaticFieldLeak")
    var service: FirebaseMessaging = FirebaseMessaging.getInstance()

    fun getToken( ) {
        service.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.i( "Fetching FCM registration token failed" + task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            val msg = token
            Timber.i( "FCM msg: " + msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    fun subToTopic(){
        Firebase.messaging.subscribeToTopic("weatherEvents")
            .addOnCompleteListener { task ->
                var msg = "Subscribed to weatherEvents topic"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Timber.i( "############# $msg #########")
            }
    }

//    /**
//     * Called if the FCM registration token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the
//     * FCM registration token is initially generated so this is where you would retrieve the token.
//     */
    override fun onNewToken(token: String) {
       Timber.i( "############# Refreshed token: $token")


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
    }





}
