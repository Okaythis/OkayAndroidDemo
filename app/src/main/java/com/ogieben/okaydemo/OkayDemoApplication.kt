package com.ogieben.okaydemo

import android.app.Application
import com.ogieben.okaydemo.logger.OkayDemoLogger
import com.protectoria.psa.PsaManager

class OkayDemoApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        val psaManager = PsaManager.init(this, OkayDemoLogger())
        psaManager.setPssAddress(BuildConfig.SERVER_URL)
    }
}