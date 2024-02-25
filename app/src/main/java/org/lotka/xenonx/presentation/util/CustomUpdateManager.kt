package org.lotka.xenonx.presentation.util

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.content.pm.PackageInfoCompat
import com.google.firebase.analytics.FirebaseAnalytics
import org.lotka.xenonx.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Singleton


@Singleton
class CustomUpdateManager(val context: Context, val firebaseAnalytics: FirebaseAnalytics ) {

    private var localAppVersion = -1
    var downloadLink = ""
    var update_features = listOf<String>()

     private val _updateStatus = MutableStateFlow(UpdateType.UNKNOWN) // Default is 'UNKNOWN'
    val updateStatus: StateFlow<UpdateType> = _updateStatus.asStateFlow()


    private val _autoUpdateEnabled = MutableStateFlow(false) // Default is 'false'
    val autoUpdateEnabled: StateFlow<Boolean> = _autoUpdateEnabled.asStateFlow()




     fun setUpdateStatus(type: UpdateType) {
        _updateStatus.value = type
    }



    private fun setAutoUpdateEnabled(isEnabled: Boolean) {
        _autoUpdateEnabled.value = isEnabled
    }


    private fun getVersionCodeSafely(context: Context): Long {
        // Attempt to get the version code from the PackageManager.
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Use getLongVersionCode() for API level 28 and above.
                PackageInfoCompat.getLongVersionCode(packageInfo)
            } else {
                // Use versionCode directly for API level 27 and below.
                packageInfo.versionCode.toLong()
            }
        } catch (e: Exception) {
            // If there is an exception, first check if BuildConfig has a valid version code.
            if (org.lotka.xenonx.BuildConfig.VERSION_CODE != 0) {
                org.lotka.xenonx.BuildConfig.VERSION_CODE.toLong()
            } else {
                // If BuildConfig.VERSION_CODE is not set correctly, return -1.
                -1L
            }
        }
    }



    enum class UpdateType {
            FORCE_UPDATE,      // The user has to forcibly update the app.
            OPTIONAL_UPDATE,   // There's an optional dialog named 'optional'.
            INDICATED_UPDATE,// The update is indicated in settings and dashboard.
            NO_UPDATE  , // There's no update available , you are using latest version.
            UNKNOWN, // Unknown version
            EXCEPTION //error in getting version
    }


     fun setAppVersionData(
        updateFunctionality: Boolean,
        autoUpdateEnabled: Boolean,
        indicatedUpdate: Int,
        optionalUpdate: Int,
        forceUpdate: Int,
        downloadLink: String,
        updateFeatures: List<String>
    ) {
        // if cdn set updateFunctionality to false
        // tell user that every thing is OK and there is no update
        if (!updateFunctionality) {
            setUpdateStatus(UpdateType.NO_UPDATE)
            return
        }




        //it may cant get app version
        //show error message and stay on splash screen
        try {
            localAppVersion  = getVersionCodeSafely(context).toInt()
        }catch (e: Exception){
            firebaseAnalytics.logEvent("FATAL_WARNING_cant_get_app_version", Bundle())
            setUpdateStatus(UpdateType.EXCEPTION)
            return
        }
        //second attempt to check app version is retrived or not
        if (localAppVersion < 70) {
            firebaseAnalytics.logEvent("FATAL_WARNING_cant_get_app_version", Bundle())
            setUpdateStatus(UpdateType.EXCEPTION)
            return
        }

        //if user is using is OK and server data is retrieved
        if(indicatedUpdate==-1){
            firebaseAnalytics.logEvent("ANDROID_FAT_indicated_version_minus", Bundle())
        }
        if (optionalUpdate ==-1) {
            firebaseAnalytics.logEvent("ANDROID_FAT_optional_version_minus", Bundle())
        }
        if (forceUpdate ==-1) {
            firebaseAnalytics.logEvent("ANDROID_FAT_force_version_minus", Bundle())
        }

        //update logic

        // Enable auto-update if the feature is enabled.
        if (autoUpdateEnabled) {
            setAutoUpdateEnabled(true);
        }

        // Check and set the update status based on the app version.
        if (localAppVersion < forceUpdate) {
            setUpdateStatus(UpdateType.FORCE_UPDATE);
        } else if (localAppVersion < optionalUpdate) {
            setUpdateStatus(UpdateType.OPTIONAL_UPDATE);
        }
         if (localAppVersion < indicatedUpdate) {
            setUpdateStatus(UpdateType.INDICATED_UPDATE);
        } else if (localAppVersion == indicatedUpdate) {
            setUpdateStatus(UpdateType.NO_UPDATE);
        }

        this.downloadLink = downloadLink
        this.update_features = updateFeatures


    }



}


