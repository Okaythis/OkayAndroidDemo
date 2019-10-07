package com.ogieben.okaydemo.fcm

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ogieben.okaydemo.data.repository.PreferenceRepo
import com.ogieben.okaydemo.ui.main.MainActivity
import com.protectoria.psa.PsaManager

class OkayDemoFirebaseMessagingService : FirebaseMessagingService() {



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        token?.run {
            PreferenceRepo(this@OkayDemoFirebaseMessagingService).putExternalId(token)
        }
    }

    override fun onMessageReceived(remoteData: RemoteMessage) {
        Log.d("Firebase Messaging", "Message from firebase ${remoteData!!.from!!}")

        if(remoteData.data.isNotEmpty()){
            // handle notification
            val notificationData = NotificationHandler.extractRemoteData(remoteData)
            Log.d("Firebase", "${notificationData!!.sessionId!!} ")

            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(ACTIVITY_WAKE_UP_KEY,  notificationData.sessionId!!.toLong() )
            })
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    companion object {
        val ACTIVITY_WAKE_UP_KEY = "wake_up_key"
    }
}