package com.okaythis.androiddemo.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.itransition.protectoria.psa_multitenant.protocol.scenarios.linking.LinkingScenarioListener
import com.itransition.protectoria.psa_multitenant.state.ApplicationState
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.PsaConstants
import com.protectoria.psa.api.converters.PsaIntentUtils
import com.protectoria.psa.api.entities.SpaEnrollData
import com.protectoria.psa.dex.common.data.enums.PsaType
import com.okaythis.androiddemo.BuildConfig
import com.okaythis.androiddemo.R
import com.okaythis.androiddemo.data.model.AuthorizationResponse
import com.okaythis.androiddemo.data.model.OkayLinking
import com.okaythis.androiddemo.data.repository.PreferenceRepo
import com.okaythis.androiddemo.fcm.OkayDemoFirebaseMessagingService
import com.okaythis.androiddemo.retrofit.RetrofitWrapper
import com.okaythis.androiddemo.ui.theme.BaseTheme
import com.okaythis.androiddemo.utils.PermissionHelper
import com.protectoria.psa.api.entities.SpaAuthorizationData
import com.protectoria.psa.api.entities.TenantInfoData

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceRepo: PreferenceRepo
    private val permissionHelper = PermissionHelper(this)
    private val retrofitWrapper = RetrofitWrapper()
    private val transactionHandler =  retrofitWrapper.handleTransactionEndpoints()
    private val userExternalID = "replaceWithYouUserExternalId2"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        preferenceRepo = PreferenceRepo(this)

        checkPermissions()
        fetchInstanceId()
        handleIntent(intent)

        enrollmentButton.setOnClickListener { view ->
            beginEnrollment()
        }

        linkingButton.setOnClickListener{
            startServerLinking(userExternalID)
        }

        authorizeButton.setOnClickListener {
            startServerAuthorization(userExternalID)
        }

        manualLinkButton.setOnClickListener{
            val linkingCode =  linkingCodeEditText.text.toString()
            if(linkingCode.isEmpty()){
                Toast.makeText(this, "Linking code can't be empty. Please enter linking code in the input field ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            linkUser(linkingCode)
        }

        pinAuthorizationButton.setOnClickListener{
            startPinAuthorization(userExternalID)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun handleIntent(intent: Intent?) {
        intent?.apply {
            val sessionId =  getLongExtra(OkayDemoFirebaseMessagingService.ACTIVITY_WAKE_UP_KEY, 0)
            if (sessionId > 0)  {
                Toast.makeText(this@MainActivity, "Current sessionId $sessionId ", Toast.LENGTH_LONG).show()
                startAuthorization(sessionId)
            }
        }
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

    private fun linkUser(linkingCode: String) {
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

        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Authorization granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Authorization not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startAuthorization(sessionId: Long) {
        PsaManager.startAuthorizationActivity(this, SpaAuthorizationData(sessionId,
            preferenceRepo.appPNS,
            BaseTheme(this).DEFAULT_PAGE_THEME,
            PsaType.OKAY, TenantInfoData("Okay AS", "")
        ))
    }

    private fun startServerAuthorization(userExternalId: String?) {
        transactionHandler.authorizeTransaction(userExternalId).enqueue(object: Callback<AuthorizationResponse> {
            override fun onFailure(call: Call<AuthorizationResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error making request to Server", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<AuthorizationResponse>,
                response: Response<AuthorizationResponse>
            ) {
                Toast.makeText(this@MainActivity, "Request made successfully ", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun startServerLinking(userExternalId: String?) {
       transactionHandler.linkUser(userExternalId).enqueue(object: Callback<OkayLinking>{
           override fun onFailure(call: Call<OkayLinking>, t: Throwable) {
               Toast.makeText(this@MainActivity, "Error making request to Server ${t.localizedMessage}", Toast.LENGTH_LONG).show()
               t.printStackTrace()
           }

           override fun onResponse(call: Call<OkayLinking>, response: Response<OkayLinking>) {
               linkUser(response?.body()!!.linkingCode)
           }

       })

    }

    private fun startPinAuthorization(userExternalId: String?) {
        transactionHandler.authorizePinTransaction(userExternalId).enqueue(object: Callback<AuthorizationResponse> {
            override fun onFailure(call: Call<AuthorizationResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error making request to Server", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<AuthorizationResponse>,
                response: Response<AuthorizationResponse>
            ) {
                Toast.makeText(this@MainActivity, "Request made successfully ", Toast.LENGTH_LONG).show()
            }

        })
    }


}
