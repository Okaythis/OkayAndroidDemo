package com.okaythis.myokaycashapp.fcm

import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.okaythis.myokaycashapp.BuildConfig.DEBUG

class NotificationHandler {

    companion object {
        private val gson = Gson()

        fun extractRemoteData(remoteData: RemoteMessage): NotificationDataContent? {
            val extractedData = NotificationData(
                NotificationType.creator(remoteData.data["type"]!!.toInt()),
                remoteData.data["data"]!!)
            try {
                return gson.fromJson(extractedData.data, NotificationDataContent::class.java)
            }catch (e: Exception){
                if(DEBUG) {
                    e.printStackTrace()
                }
                null
            }

            return null
        }
    }
}