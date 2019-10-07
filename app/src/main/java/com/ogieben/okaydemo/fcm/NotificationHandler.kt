package com.ogieben.okaydemo.fcm

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.ogieben.okaydemo.BuildConfig.DEBUG
import java.lang.Exception

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