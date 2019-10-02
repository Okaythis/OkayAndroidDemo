package com.ogieben.okaydemo.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.ogieben.okaydemo.data.repository.PreferenceRepo

class OkayDemoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        token?.run {
            PreferenceRepo(this@OkayDemoFirebaseMessagingService).putExternalId(token)
        }
    }
}