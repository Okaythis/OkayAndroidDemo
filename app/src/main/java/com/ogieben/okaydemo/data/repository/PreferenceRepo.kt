package com.ogieben.okaydemo.data.repository

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PreferenceRepo(context: Context) {

    private val prefStorage: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

    fun persistAppPns(instanceId: String) {
//        prefStorage ?: return with(prefStorage.edit()) {
//            putString(APP_PNS, instanceId)
//            commit()
//        }

        prefStorage.edit().putString(APP_PNS, instanceId).apply()
    }

    fun getAppPns(): String? = prefStorage.getString(APP_PNS, null)

    fun saveExternalID(externalId:String) = prefStorage.edit().putString(EXTERNAL_ID, externalId).apply()

    companion object {
        const val PREFERENCE_KEY = "firebase_instance_id"
        const val APP_PNS = "app_pns"
        const val EXTERNAL_ID = "external_id"
    }
}