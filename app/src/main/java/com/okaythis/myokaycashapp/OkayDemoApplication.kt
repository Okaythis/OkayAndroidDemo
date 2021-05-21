package com.okaythis.myokaycashapp

import android.app.Application
import com.itransition.protectoria.psa_multitenant.restapi.GatewayRestServer
import com.ogieben.okaydemo.logger.OkayDemoLogger
import com.okaythis.okaythis_flutter_plugin.resourceprovider.DefaultResourceProvider
import com.protectoria.psa.PsaManager
import com.protectoria.psa.dex.common.data.json.PsaGsonFactory
import com.protectoria.psa.dex.ui.texts.TransactionResourceProvider

class OkayDemoApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        initPsa()
        initGatewayServer()
    }

    private fun initPsa() {
        val psaManager = PsaManager.init(this, OkayDemoLogger())
        psaManager.setPssAddress(BuildConfig.SERVER_URL)
    }

    private fun initGatewayServer() {
        GatewayRestServer.init(PsaGsonFactory().create(), BuildConfig.SERVER_URL + "/gateway/")
    }
}
