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
import com.ogieben.okaydemo.BuildConfig
import com.ogieben.okaydemo.R
import com.ogieben.okaydemo.data.repository.PreferenceRepo
import com.ogieben.okaydemo.utils.PermissionHelper
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.PsaConstants
import com.protectoria.psa.api.converters.PsaIntentUtils
import com.protectoria.psa.api.entities.SpaEnrollData
import com.protectoria.psa.dex.common.data.enums.PsaType

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var preferenceRepo: PreferenceRepo
    private val permissionHelper = PermissionHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        preferenceRepo = PreferenceRepo(this)

        checkPermissions()
        fetchInstanceId()
        fab.setOnClickListener { view ->
            beginEnrollment()
        }
    }

    private fun beginEnrollment() {
        val appPns = preferenceRepo.getAppPns()
//        Toast.makeText(this@MainActivity, appPns + " " + BuildConfig.PUB_PSS_B64 + " " + BuildConfig.INSTALLATION_ID , Toast.LENGTH_LONG).show()
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

    private fun fetchInstanceId () {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("", "getInstanceId failed", task.exception)
                    Toast.makeText(this@MainActivity, "Error could not fetch token", Toast.LENGTH_LONG).show()
                    return@OnCompleteListener
                }
                val token = task.result?.token
                preferenceRepo.persistAppPns(token.toString())
//                Toast.makeText(this@MainActivity, token, Toast.LENGTH_LONG).show()
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_ENROLL) {
            if (resultCode == RESULT_OK) {
                //We should save data from Enrollment result, fo future usage
                data?.run {
                    val resultData = PsaIntentUtils.enrollResultFromIntent(this)
                    resultData.let {
                        preferenceRepo.saveExternalID(it.externalId)
                    }
                    Toast.makeText(applicationContext,   "Successfully got this externalId " + resultData.externalId, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Error Retrieving intent after enrollment", Toast.LENGTH_SHORT).show()
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
