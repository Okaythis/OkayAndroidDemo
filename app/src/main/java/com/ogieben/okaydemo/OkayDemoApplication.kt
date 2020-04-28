package com.ogieben.okaydemo

import android.app.Application
import android.content.Context
//import androidx.multidex.MultiDex
import com.itransition.protectoria.psa_multitenant.restapi.GatewayRestServer
import com.ogieben.okaydemo.data.repository.ResourceProviderImpl
import com.ogieben.okaydemo.logger.OkayDemoLogger
import com.protectoria.psa.PsaManager
import com.protectoria.psa.dex.common.data.json.PsaGsonFactory

class OkayDemoApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        initPsa()
//        initGatewayServer()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        MultiDex.install(this);
    }

    private fun initPsa() {
//        val psaManager = PsaManager.init(this, OkayDemoLogger())
        val psaManager = PsaManager.init(this, OkayDemoLogger(), ResourceProviderImpl())
        psaManager.setPssAddress(BuildConfig.SERVER_URL)
    }


    private fun initGatewayServer() {
        GatewayRestServer.init(PsaGsonFactory().create(), BuildConfig.SERVER_URL + "/gateway/")
    }
}
