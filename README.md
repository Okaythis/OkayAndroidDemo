# Demo Application which demonstrates how to use Okay Android SDK with your custom Server

## How to use this app

**Note:** Our server and mobile App uses this endpoint **http://protdemo.demohoster.com** as our PSS base url(since we are testing). It is important that these urls are the same, otherwise linking and authorization won't work, as we need them to use the same server.

This application illustrates some simple use cases for enrollment, linking and authorization. It requires a server to function properly, so we built one in Nodejs, which can be found on this repository https://github.com/Okaythis/OkayNodeJs.

The server has three endpoints which are used to inititate enrollment, linking and two different types of authorization(OKAY and PIN).

In order to get the best out of this app, we recommend you download the server and run it locally. All information regarding the installation and testing can be found on this page https://github.com/Okaythis/OkayNodeJs.

The three endpoints we will be using are listed below (I assume you are running the server on your local machine):

```js
// Endpoint for linking a user with Okay Server
http://localhost:4000/link?userExternalId=USER_EXTERNAL_ID

// Endpoint for initiating a simple OKAY authorization 
http://localhost:4000/auth?userExternalId=USER_EXTERNAL_ID

// Endpoint for initiating an authorization with PIN 
http://localhost:4000/auth/pin?userExternalId=USER_EXTERNAL_ID

```
### Authorization Types Explained:

**OKAY Authorization:** This authorization is a very simple auth type that just requires you to click the **OKAY** button on the auth screen as displayed in the image below to authorize a transaction.

<img src="https://github.com/Okaythis/OkayNodeJs/blob/master/app/public/images/auth-screen.png" alt="Authorization Screen" width="400" />

**PIN AUTHORIZATION:** This authorization type requires a pin in order to complete the authorization flow. The difference between these two authorization types is that **OKAY**  authorization does not require a pin to complete a transaction while **PIN** authorization requires a pin.

**Note:** There are more than two authorization types, but for brievity we just explained the ones used in this app.

We also recommend you install **Postman** for testing your queries, as this is what we will be using for testing. Feel free to use curl or any other tool you feel comfortable with.

## How we use these enpoints on the server.

### Linking Endpoint
Before we use this endpoints, we will need to enroll the user with Okay SDK then retreive the user's `externalId` from Okay SDK after enrollment or we could just create a `UUID` as our `externalId` for our user after enrollment. Given that enrollment is being handled by Okay SDK, we will explain how to initiate enrollment in the **Enrollment with Okay SDK on Android** section for the SDK below.

`externalId` is a unique identifier the we use to identify different users that we link to our tenant. Any unique identifier can work here.

After we retreive the `externalId` from Okay SDK or `UUID` we generated(our generated `UUID` becomes our `externalId`), we will send this extenalId to our server's linking url to link this user with our tenant(For more information about how we created a tenant for this server, please read the documentation [here](https://github.com/Okaythis/OkayNodeJs)). 

Let us assume we enroll a new user with Okay, let us use `xUdKijgIHFnhyi+d7pnabHS8gjHLy/qqkXtOYZkKrpo=` as our user's `externalId`. We send this `externalId` to this endpoint `http://localhost:4000/link` like so:

```js
  http://localhost:4000/link?userExternalId=xUdKijgIHFnhyi+d7pnabHS8gjHLy/qqkXtOYZkKrpo=
```

if linking was succesful after our request, we get a response body like this:

```json
{
    "linkingCode": "579813",
    "linkingQrImg": "iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAABf0lEQVR42u3aS46DMAwGYCMWLDnCHIWjNUfrUThClywQHsfOAyqoJiGj0Uh/VmnzrUjq2KbEPxkvAgMDAwP7G7aQjZF5I3YyG2Riowe7zXTO87j0wvjpVzfbnwWsAfPPfJpHXXH09RrCdoxgDZk/7eymmcB+g/HahZDcgzVkMYZQx+L9h4+hBqyIxRtQQ7SbLFZfX5RgRSyPVRYeehV+TPPASpjtgkRm8of8od/4ydNfhWB3mUZmN+XITINsh66DtWHyzGmXHoddOP4WwKqYpW0UjnSasLD3GxCsnIWiQ2rnNabHmsi9xxCwKmZ5hTx8f7ZTeqxBG6wFY7a84pAen1UfYJVMVvVIB0+hmgZrwjR0kG1Hqkfk2IPdZmksh46ERJXzIgWsiOXa2WcaqS3sJgZrwWLnJ0TmcLbd2W8BrIKltnAXgomF6IuXGmCVLDd8tn0LCKwZs0xjtWAC1oTt3nj6h6/FnU4I7D7LN2DsW/YXHQywcob/1YCBgYH9Y/YNoQbiC+wLFDIAAAAASUVORK5CYII=",
    "status": {
        "code": 0,
        "message": "OK"
    }
}
```

If this was the response we got, then we will need to retreive the `"linkingCode": "579813"` as this is the value we need to finish linking the user. We can proceed to linking this user by passing the `linkingCode` into the `EditTextField` in the app, then click the `Link Manually` button. If all goes well you should see a toast display **Linking Successful** on the mobile app. If linking was successful, we can now start authorization request from our server.

### Authorization Endpoint

**OKAY Authorization:** We can now start our authorization process from our server using the user's `externalId` retrieved from the user's enrollment like so.

```js
  http://localhost:4000/auth?userExternalId=xUdKijgIHFnhyi+d7pnabHS8gjHLy/qqkXtOYZkKrpo=
```

If this request was handled successfully, Okay server will send a push notication to the mobile app, that will display the authorization screen, allowing the user to **OK** the transaction or probally decline the request. You will see a screen like the one below.

<img src="https://github.com/Okaythis/OkayNodeJs/blob/master/app/public/images/auth-screen.png" alt="Authorization Screen" width="400" />


**PIN Authorization:**  To start authorization with pin on Okay, we send a request to our server like so passing the `externalId`as query parameter like so:


```js
  http://localhost:4000/auth/pin?userExternalId=xUdKijgIHFnhyi+d7pnabHS8gjHLy/qqkXtOYZkKrpo=
```

If this request was handled successfully, Okay server will send a push notication to the mobile app, that will display the authorization screen, allowing the user to enter a pin in order ot complete the transaction or probally decline the request. As of now just type in any pin to complete the transaction. The pin that was entered by the user will be sent as body parameters to your tenant callbeck(To understand how to set a callback for your tenant please see this [documentation](https://github.com/Okaythis/OkayNodeJs)). Remember, you are responsible for generating and validating the pins that your users will use in your production server.


Now that we know how these enpoints work we can now call them from our app when we host this server in the cloud.

## Enrollment with Okay SDK on Android

This is a simple guide on how to integrate and initiate enrollment with Okay SDK.

We will begin by creating a new Android project on Android Studio.

Before we begin enrollment with Okay on our Android app, we will need to add Okay as a dependency to our app level `build.gradle` file (i.e `app\build.gradle`).

```gradle

// app/build.gradle

...

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation"org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.core:core-ktx:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'com.google.android.material:material:1.0.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test:runner:1.2.0'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

    // Okay dependency
    implementation 'com.okaythis.sdk:psa:1.1.0'
}

```

We will also be adding Okay's maven repository to your project level `build.gradle` file.

```gradle
// project/build.gradle

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url 'https://dl.bintray.com/okaythis/maven'
        }
    }
}

```

We will also need to set up Firebase for our project. If you are not familiar with integrating Firebase messaging please check this [documentaion](https://firebase.google.com/docs/cloud-messaging/android/client) for more information as Okay SDK depends on it.

```gradle
// app/build.gradle

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation"org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.core:core-ktx:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'com.google.android.material:material:1.0.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test:runner:1.2.0'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

    // Okay dependency
    implementation 'com.okaythis.sdk:psa:1.1.0'

    // Firebase Dependency
    implementation 'com.google.firebase:firebase-messaging:20.0.0'
}
```

We can now sync our app's gradle file to build.

## Init PSA SDK

In order for Okay SDk to work correctly we will need to sync the SDK with PSS. Initialization of the PSA should be done within our Application class, inside the `onCreate()` method.

We use the `PsaManager` class from Okay to initialize our PSA. We will be using two methods from the `PsaManager` class, the `init()` and `setPssAddress()` methods. The `init()` and `setPssAddress()`  method from the `PsaManager` class has the following structure.

```java

  PsaManager psaManager = PsaManager.init(Context c, T extends ExceptionLogger)

  // The PSS_SERVER_ENDPOINT is the url address for our PSS service e.g http://protdemo.demohoster.com
  // This PSS_SERVER_ENDPOINT  must be the same endpoint we use on our our backend. In this case 
  // both server and app use http://protdemo.demohoster.com
  psaManager.setPssAddress(PSS_SERVER_ENDPOINT);
```

A typical illustration of how our `Application` class should be

```kotlin

// OkayDemoApplication.kt

class OkayDemoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initPsa()
    }

    private fun initPsa() {
        val psaManager = PsaManager.init(this, OkayDemoLogger())
        psaManager.setPssAddress(BuildConfig.SERVER_URL)
    }
}
```

This is what my `OkayDemoLogger` class looks like.

```kotlin
  class OkayDemoLogger: ExceptionLogger {
      override fun setUserIdentificator(p0: String?) {
          Log.e("SET ID: ", "Successfully set user identificator $p0 ")
      }

      override fun exception(p0: String?, p1: Exception?) {
          Log.e("Exception: ", "Okay Error $p0 -- Exception: $p1")
      }

  }
```

We will need to add our application class to our manifest file by adding `android:name=".OkayDemoApplication"` to our application tag.

```xml

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.ogieben.okaydemo">

    <application
      android:name=".OkayDemoApplication" 
      android:allowBackup="true"
      android:icon="@mipmap/ic_launcher"
      android:label="@string/app_name"
      android:roundIcon="@mipmap/ic_launcher_round"
      android:supportsRtl="true"
      android:theme="@style/AppTheme">

      ...
  
    </application>

```
### Permissions

The Okay SDK requires certain kinds of permissions to work properly. We will have to ask the users to grant these permissions before we proceed.  We can easily create these helper methods to handle permission resolution for us.

```kotlin
// PermissionHelper.kt

class PermissionHelper(private val activity: Activity) {

    val REQUEST_CODE = 204

    fun hasPermissions(ctx: Context, permission: Array<String>): Boolean = permission.all {
        ActivityCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(permission: Array<String>) = ActivityCompat.requestPermissions(activity, permission, REQUEST_CODE)
}

```

The Okay SDK comes with a prepacked method `PsaManager.getRequiredPermissions()` that helps us fetch an array of all required permissions.

```kotlin
// MainActivity.kt

  val permissionHelper = PermissionHelper(activity)

  private fun checkPermissions() {
        // prepacked method
        val requiredPermissions = PsaManager.getRequiredPermissions()
        if (!permissionHelper.hasPermissions(this, requiredPermissions)) {
            permissionHelper.requestPermissions(requiredPermissions)
        }
    }

```

We can now use the `checkPermission()` method within our `MainActivity.kt`'s `onCreate` method to request for all permission.

```kotlin
// MainActivity.kt

 override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    checkPermissions()
  }

```

We will need our device token from Firebase to be able to finish our enrollment.

If we have firebase successfully setup, we could request for our device token using the code sample below. If you have note been able to setup Firebase please refer to this [documentaion](https://firebase.google.com/docs/cloud-messaging/android/client) as Okay requires this service to work correctly.

```kotlin
// MainActivity.kt
 private var preferenceRepo: PreferenceRepo = PreferenceRepo(context)

 private fun fetchInstanceId () {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("", "getInstanceId failed", task.exception)
                    Toast.makeText(this@MainActivity, "Error could not fetch token", Toast.LENGTH_LONG).show()
                    return@OnCompleteListener
                }
                val token = task.result?.token

                // save token to SharedPreference storage for easy retrieval
                preferenceRepo.persistAppPns(token.toString())
            })
    }

```

If all is good we can now invoke this method from our `onCreate()` method within our activity like so.

```kotlin
class MainActivity : AppCompatActivity() {
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // request permissions
        checkPermissions()

        // fetch token
        fetchInstanceId()
    }

}

```

We can now proceed with our enrollment if all permissions has been granted and our appPns(also know as Firebase token) has been retrieved successfully.

```kotlin

 private fun beginEnrollment() {
  
    val appPns = preferenceRepo.getAppPns() // retrieve Firebase token from SharedPreference storage

    // 
    val pubPssB64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxgyacF1NNWTA6rzCrtK60se9fVpTPe3HiDjHB7MybJvNdJZIgZbE9k3gQ6cdEYgTOSG823hkJCVHZrcf0/AK7G8Xf/rjhWxccOEXFTg4TQwmhbwys+sY/DmGR8nytlNVbha1DV/qOGcqAkmn9SrqW76KK+EdQFpbiOzw7RRWZuizwY3BqRfQRokr0UBJrJrizbT9ZxiVqGBwUDBQrSpsj3RUuoj90py1E88ExyaHui+jbXNITaPBUFJjbas5OOnSLVz6GrBPOD+x0HozAoYuBdoztPRxpjoNIYvgJ72wZ3kOAVPAFb48UROL7sqK2P/jwhdd02p/MDBZpMl/+BG+qQIDAQAB"
    val installationId = "9990"

    val spaEnroll = SpaEnrollData(appPns,
    pubPssB64,
    installationId,
    null,
    PsaType.OKAY)
    PsaManager.startEnrollmentActivity(this, spaEnroll)
   }

```

If the `beginErollment()` method was called successfully we will need a way to retrieve information from the enrollment Activity. So will override the `onActivityResult()` method within our `MainActivity.kt` class.

```kotlin
// MainActivity.kt

 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_ENROLL) {
            if (resultCode == RESULT_OK) {
                //We should save data from Enrollment result, for future usage
                data?.run {
                    val resultData = PsaIntentUtils.enrollResultFromIntent(this)
                    resultData.let {
                      // retreive externalId here from Okay SDK
                        preferenceRepo.saveExternalID(it.externalId)
                    }
                    Toast.makeText(applicationContext,   "Successfully got this externalId " + resultData.externalId, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Error Retrieving intent after enrollment", Toast.LENGTH_SHORT).show()
            }
        }
    }

```


## Linking a user with Okay Sdk 

In order to successfully finish the initialization stage we need to link the user with Okay. This allows us to authorize/authenticate a particular user's action.`

Note: This section of the Okay SDK requires interaction with a dedicated server that sits as a middleman between PSS and PSA. We highly recommend that you have this server built/running while going through this section of the documentation. 

To enable linking on the your app you will need to initialize **Okay** `GatewayRestServer` in your app's `Application` file. In this  case it is going to be `OkayDemoApplication.kt`.

```kotlin
class OkayDemoApplication: Application() {


  override fun onCreate() {
      super.onCreate()
      initPsa()
      // Added this method call
      initGatewayServer()
  }

  private fun initPsa() {
      val psaManager = PsaManager.init(this, OkayDemoLogger())
      psaManager.setPssAddress(BuildConfig.SERVER_URL)
  }

  // Added this method 
  private fun initGatewayServer() {
      GatewayRestServer.init(PsaGsonFactory().create(), BuildConfig.SERVER_URL + "/gateway/")
  }
}

```


We will send a request to our server to start the linking process. We will be sending the ***externalId*** generated from Okay SDK(or any `UUID` you can create for this paticular user) as a parameter to our server. If our request was processed successfully we will recieve a response with the **linkingCode** required to complete the linking. The linkCode is a six digit number generated for this purpose. For illustration we will be using this server `https://okayserversample.herokuapp.com/` as test server for this section. 

To illustration how linking on the backend, please send a **POST** request to this endpoint `https://okayserversample.herokuapp.com/link?userExternalId=REPLACE_WITH_EXTERNAL_ID_HERE`. This is a light weight demo server we built just to test this app. You should receive a response with the structure below.

```json
{
  "linkingCode": "680215",
  "linkingQrImg": "iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAABe0lEQVR42u3aQY6DMAwFUFcsWHIEjsLRyNE4CkdgyQLhie2E0IpWJcloNNLPKoW3SiLHNiX+ZiwEBgYGBvY3bCUbHfNO7Pys9RMbDVgx0znP3dp4xpO83W1/VrAKTNZ8mDt946hf2rAdHVhFJqed3TAT2G8w3h4hJDdgFVmMIfRg7+XHx1ADdovFG1BDtBssVr+/KMFusTQ2/2LUq/Bjmgd2h9ku+MhMcshHfSKTSa5CsFJmWfHAR15Brd8OiSovhxwsh8mav6THcRfAyhlbKRceWnos4+KQg2WwjdKw9FgTual/SjDA8pgecll8olN6bPUIWDkL2ZpWHyk9dtoLAqvB7EhTzN9CygFWhTFb/ibbMaZ6pGewcnaMEExCR8KBVWHPtfPRFnYDg9VgsfOjjTV3TuQuP2qA3WTndMJpEa0h+s1HDbBctsegsp9bQGB1WGgLL9aamAisCjt33f3ia3GnEwIrZ+kG5NC3bPiqdgbLYfhfDRgYGNg/Zj/Eb5QIcNwjvgAAAABJRU5ErkJggg==",
  "status": {
      "code": 0,
      "message": "OK"
  }
}
```

After we successfully generated the linking code we can now proceed to linking the user with Okay SDK, by entering the `linkingCode` from the response into the app and clicking the `Link Manually` button on the app.

**PsaManager** provides us with a helper function that allows us to link users with SPS right from Okay SDK. The structure of the method we can use is like so.

```kotlin
PsaManager.linkTenant(linkingCode: String, spaStorage: SpaStorage, linkingScenarioListener: LinkingScenarioListener)
```

The **LinkingScenarioListener** must be implemented, as it allows us to listen for two possible events: **onLinkingCompletedSuccessful** and **onLinkingCompletedSuccessful**. We will be implementing this listener soon.

We will also need to implement the **SpaStorage** interface in our application. I think the easiest place to do this, is from one of our repositories(**PreferenceRepo** class in this case). Of course this is just for convenience.

Below is a typical example of what my **PreferenceRepo** class might look like.

```kotlin
class PreferenceRepo(context: Context): SpaStorage {

    private val prefStorage: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
    
    override fun getPubPssBase64(): String? {
        return prefStorage.getString(PUB_PSS_B64, "")
    }

    override fun putAppPNS(p0: String?) {
        with(prefStorage.edit()) {
            putString(APP_PNS, p0)
            commit()
        }
    }

    override fun putPubPssBase64(p0: String?) {
        with(prefStorage.edit()) {
            putString(PUB_PSS_B64, p0)
            commit()
        }
    }

    override fun getAppPNS(): String? {
        return prefStorage.getString(APP_PNS, "")
    }

    override fun getEnrollmentId(): String? {
        return prefStorage.getString(ENROLLMENT_ID, "")
    }

    override fun putInstallationId(p0: String?) {
        with(prefStorage.edit()) {
            putString(INSTALLATION_ID, p0)
            commit()
        }
    }

    override fun putExternalId(p0: String?) {
        with(prefStorage.edit()) {
            putString(EXTERNAL_ID, p0)
            commit()
        }
    }

    override fun putEnrollmentId(p0: String?) {
        with(prefStorage.edit()) {
            putString(ENROLLMENT_ID, p0)
            commit()
        }
    }

    override fun getInstallationId(): String? {
        return prefStorage.getString(INSTALLATION_ID, "")
    }

    override fun getExternalId(): String? {
        return prefStorage.getString(EXTERNAL_ID, "")
    }

    companion object {
        const val PREFERENCE_KEY = "firebase_instance_id"
        const val APP_PNS = "app_pns"
        const val EXTERNAL_ID = "external_id"
        const val PUB_PSS_B64 = "pub_pss_b64"
        const val ENROLLMENT_ID = "enrollment_id"
        const val INSTALLATION_ID = "installation_id"
    }
}

```

Next we create a method that takes as input the `linkingCode` and then inititates the linking call to `PsaManager.linkTenant()`.

```kotlin
// MainActivity.kt

private var preferenceRepo = PreferenceRepo(this@MainActivity)
 
fun linkUser(linkingCode: String) {
    // grab Psa instance
    val psaManager = PsaManager.getInstance()

    // LinkingScenarioListener listener
    val linkingScenarioListener: LinkingScenarioListener = object: LinkingScenarioListener{
        override fun onLinkingCompletedSuccessful(var1: Long, var3: String){
            Toast.makeText(this@MainActivity, "Linking Successful", Toast.LENGTH_LONG).show()
        }

        override fun onLinkingFailed(var1: ApplicationState) {
            Toast.makeText(this@MainActivity, "Linking not Successful: linkingCode: ${linkingCodeEditText.text} errorCode: ${var1.code} ", Toast.LENGTH_LONG).show()
        }
    }
    // call Psa linkTenant method
    psaManager.linkTenant(linkingCode, preferenceRepo, linkingScenarioListener)
}

```

We can now initiate the call to **linkUser()** from our apps onCreate method like so.

```kotlin
// MainActivity.kt

 override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    checkPermissions()
    fetchInstanceId()
    enrollment_button.setOnClickListener { view ->
        beginEnrollment()
    }

    manualLinkButton.setOnClickListener{
       // retreive the linkingCode (i.e the linkingCode returned from our test server) from the EditTextView entered into the app
        val linkingCode =  linkingCodeEditText.text.toString()

        if(linkingCode.isEmpty()){
            Toast.makeText(this, "Linking code can't be empty. Please enter linking code in the input field", Toast.LENGTH_LONG).show()
            return@setOnClickListener
        }
        linkUser(linkingCode)
    }
  }
```


## Authorizing a Transaction with Okay 

If we have successfully linked our user we can now proceed to authorizing transactions or authenticating users.

The steps are pretty straight forward. We make a request to our server(SPS) to begin our authorization, Our server will make a call to PSS, PSS in turn, will send a push notification to our app with the current `tenantId` of our server and the current transaction `sessionId` fields. Once we receive the push notification from Okay Servers we can now proceed to using the SDK's `PsaManager.startAuthorizationActivity(activity: Activity, spaAuthorizationData :SpaAuthorizationData)` method,  passing in the current instance of our activity and an instance of SpaAuthorizationData class.

Starting an authorization begins with a simple request (POST) to our demo server like so. To see how authorization is started on the server, please send a **POST** request to this endpoint `https://okayserversample.herokuapp.com/auth?userExternalId=REPLACE_WITH_EXTERNAL_ID_HERE`. This is a light weight demo server we built just to test this app. You should receive a response with the structure below.

```json
{
  "sessionExternalId": "320045",
  "status": {
      "code": 0,
      "message": "OK"
  }
}
```

Our application will recieve a Push Notification that will be handled by our FirebaseMassagingService (This service is part of Firebase messaging if you are yet to setup Firebase please see this [documentaion](https://firebase.google.com/docs/cloud-messaging/android/client)). In this illustration we extend this service in our app using the `OkayDemoFirebaseMessagingService` class. 

This is what our `OkayDemoFirebaseMessagingService` class looks like.

```Kotlin
// OkayDemoFirebaseMessagingService.kt

class OkayDemoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        token?.run {
            PreferenceRepo(this@OkayDemoFirebaseMessagingService).putExternalId(token)
        }
    }

    override fun onMessageReceived(remoteData: RemoteMessage) {

        if(remoteData.data.isNotEmpty()){
            // handle notification
            val notificationData = NotificationHandler.extractRemoteData(remoteData)

            // You can handle the data from the push notification here however you seem fit,
            // But in this illustration we just send sessionId as an Intent extra to MainActivity
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(ACTIVITY_WAKE_UP_KEY,  notificationData.sessionId!!.toLong() )
            })
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    companion object {
        val ACTIVITY_WAKE_UP_KEY = "wake_up_key"
    }
}

```

We simply receive this sessionId inside `MainActivity.kt`

```kotlin
 // MainActivity.kt

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
      // handle incoming intent
      handleIntent(intent)

      enrollmentButton.setOnClickListener { view ->
          beginEnrollment()
      }

    }

  private fun handleIntent(intent: Intent?) {
      // we retreive our sessionId from the intent,
      // and then pass it to startAuthorization() method
      intent?.apply {
          val sessionId =  getLongExtra(OkayDemoFirebaseMessagingService.ACTIVITY_WAKE_UP_KEY, 0)
          if (sessionId > 0)  {
              Toast.makeText(this@MainActivity, "Current sessionId $sessionId ", Toast.LENGTH_LONG).show()
              // Start Authorization with retrieved session Id
              startAuthorization(sessionId)
          }
      }
  }


  private fun startAuthorization(sessionId: Long) {
    // Pass in the sessionId retrieved from push notification
    PsaManager.startAuthorizationActivity(this, SpaAuthorizationData(sessionId,
        preferenceRepo.appPNS,
        BaseTheme(this).DEFAULT_PAGE_THEME,
        PsaType.OKAY))
  }

    ...
}

```

If we successfully retrieved the `sessionId` from our push notification, we pass the `sessionId` to `PsaManager.startAuthorizationActivity()` method. It is this method that starts the authorization process. The `PsaManager.startAuthorizationActivity()` has the following signature.

```kotlin
  PsaManager.startAuthorizationActivity(Activity activity, SpaAuthorizationData authorizationData)
```

It receives as arguments **Activity** and **SpaAuthorizationData** types. The **SpaAuthorizationData** takes has the following constructor signature.

```kotlin
  SpaAuthorizationData(long sessionId, String appPNS, PageTheme pageTheme, PsaType psaType)
```


