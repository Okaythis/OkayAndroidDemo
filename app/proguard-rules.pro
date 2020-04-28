# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Firebase
-keep class * extends com.google.firebase.messaging.FirebaseMessagingService
-keep class com.ogieben.okaydemo.** { *; }
-keep class com.ogieben.okaydemo.fcm { *; }
-keep class com.ogieben.okaydemo.ui.** { *; }


#-dontobfuscate
-keep class com.google.gson.** { *; }
-keep class java.** { *; }
-keep class javax.** { *; }
-keep class org.bouncycastle.** { *; }

-keep class dalvik.system.DexClassLoader { *; }
-keep class dalvik.system.DexFile { *; }

-keep class com.protectoria.** { *; }
-keep class com.itransition.** { *; }

-keep public class androidx.core.app.CoreComponentFactory
-keep public class * extends android.app.AppComponentFactory

# greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}

-keep class **$Properties

-dontwarn org.greenrobot.greendao.database.**
-dontwarn rx.**

# Gson
-keep public class com.google.gson.Gson { *; }

# Okio
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Dagger
-dontwarn com.google.errorprone.annotations.*

# AndroidPocesses
-dontwarn com.jaredrummler.android.shell.*

# Ignore DatatypeConverter used by some KeyUtils methods which are not used by mobile app
-dontwarn javax.xml.bind.DatatypeConverter

-keep public class com.protectoria.psa.PsaManager {
   public <init>(java.lang.Class);
 }
-keepclasseswithmembers public class com.protectoria.psa.** {
   public <init>(java.lang.Class);
 }

#Android
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep class * extends androidx.fragment.**

# Controllers
-keep public class * extends com.protectoria.psa.dex.core.AbstractCodeBlockController {
    public <init>(java.lang.Class);
}

-keepclassmembers public class com.protectoria.psa.dex.core.AbstractCodeBlockController {
    public void execute(com.protectoria.psa.dex.common.dynamiccode.data.DependenciesEntryPoint);
}

# pss-psa-client
-dontwarn lombok.NonNull
-keep class com.protectoria.pss.core.watermark.** { *; }

# lib-zxing-scanner
-keep class * extends android.hardware.Camera$AutoFocusCallback { *; }
-keep class * extends android.hardware.Camera$PreviewCallback { *; }

# android-processes
-keep class com.jaredrummler.android.processes.AndroidProcesses { *; }
-keep class com.jaredrummler.android.processes.models.AndroidAppProcess { *; }


-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.content.ContextWrapper {public *;}
-keep public class * extends android.app.** {public *;}
-keep public class * extends androidx.core.app.AppComponentFactory { *;}
-keep public class * extends androidx.core.** { *;}
-keep public class * implements androidx.versionedparcelable.VersionedParcelable


-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

#-dontwarn android.**
#-dontwarn androidx.**
#-dontwarn junit.**
#-dontwarn org.apache.**
#-dontwarn com.android.**
