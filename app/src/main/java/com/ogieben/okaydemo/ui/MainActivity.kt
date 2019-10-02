package com.ogieben.okaydemo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.itransition.protectoria.psa_multitenant.protocol.scenarios.linking.LinkingScenarioListener
import com.itransition.protectoria.psa_multitenant.state.ApplicationState
import com.ogieben.okaydemo.BuildConfig
import com.ogieben.okaydemo.R
import com.ogieben.okaydemo.data.model.LinkingStatus
import com.ogieben.okaydemo.data.model.OkayLinking
import com.ogieben.okaydemo.data.repository.PreferenceRepo
import com.ogieben.okaydemo.data.repository.SpaStorageManager
import com.ogieben.okaydemo.network.retrofit.RetrofitWrapper
import com.ogieben.okaydemo.network.retrofit.TransactionEndpoints
import com.ogieben.okaydemo.utils.PermissionHelper
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.PsaConstants
import com.protectoria.psa.api.converters.PsaIntentUtils
import com.protectoria.psa.api.entities.SpaEnrollData
import com.protectoria.psa.dex.common.data.enums.PsaType
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins.onError
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceRepo: PreferenceRepo
    private val permissionHelper = PermissionHelper(this)
    private val retrofitWrapper = RetrofitWrapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        preferenceRepo = PreferenceRepo(this)

        checkPermissions()
        fetchInstanceId()
        enrollment_button.setOnClickListener { view ->
            beginEnrollment()
        }

        linking_button.setOnClickListener{ view ->

            linkUser(linkingCodeEditText.text.toString())

//            retrofitWrapper
//                .handleTransactionEndpoints()
//                .linkUser("035212")
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(Consumer { t  ->
//                    Log.d("Okay testing", t.toString())
//                    linkUser("151498")
//                }, Consumer { t -> Log.d("Error handler", t.localizedMessage)  })

        }
    }

    fun errorHandler(error: Throwable) {

    }

    fun successHandler(data: LinkingStatus) {

    }

    private fun beginEnrollment() {
        val appPns = preferenceRepo.appPNS
        Toast.makeText(this@MainActivity, appPns + " " + BuildConfig.SERVER_URL + " " + BuildConfig.INSTALLATION_ID, Toast.LENGTH_LONG).show()
        val spaEnroll = SpaEnrollData(appPns,
            BuildConfig.PUB_PSS_B64,
            BuildConfig.INSTALLATION_ID,
            null,
            PsaType.OKAY)
        PsaManager.startEnrollmentActivity(this@MainActivity, spaEnroll)
    }

    private fun checkPermissions() {
        val requiredPermissions = PsaManager.getRequiredPermissions()
        if (!permissionHelper.hasPermissions(this, requiredPermissions)) {
            permissionHelper.requestPermissions(requiredPermissions)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun linkUser(linkingCode: String) {
        val psaManager = PsaManager.getInstance()
        val linkingScenarioListener: LinkingScenarioListener = object: LinkingScenarioListener{
            override fun onLinkingCompletedSuccessful(var1: Long, var3: String){
                Toast.makeText(this@MainActivity, "Linking Successful", Toast.LENGTH_LONG).show()
            }

            override fun onLinkingFailed(var1: ApplicationState) {
                Toast.makeText(this@MainActivity, "Linking not Successful: linkingCode: ${linkingCodeEditText.text} errorCode: ${var1.code} ", Toast.LENGTH_LONG).show()
            }
        }

        psaManager.linkTenant(linkingCode, preferenceRepo, linkingScenarioListener)
    }

    private fun fetchInstanceId () {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("", "getInstanceId failed", task.exception)
                    Toast.makeText(this@MainActivity, "Error could not fetch token", Toast.LENGTH_LONG).show()
                    return@OnCompleteListener
                }
                val token = task.result?.token
                preferenceRepo.putAppPNS(token.toString())
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_ENROLL) {
            if (resultCode == RESULT_OK) {
                //We should save data from Enrollment result, for future usage
                data?.run {
                    val resultData = PsaIntentUtils.enrollResultFromIntent(this)
                    resultData.let {
                        preferenceRepo.putExternalId(it.externalId)
                    }
                    Toast.makeText(applicationContext,   "Successfully got this externalId " + resultData.externalId, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Error Retrieving intent after enrollment:- code: ${linkingCodeEditText.text} errorCode: $resultCode", Toast.LENGTH_SHORT).show()
            }
        }
        // Here you can receive result of the authorization flow
//        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_AUTHORIZATION) {
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, getString(R.string.auth_success), Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, getString(R.string.auth_error), Toast.LENGTH_SHORT).show()
//            }
//        }
    }

}
