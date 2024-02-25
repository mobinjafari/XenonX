package org.lotka.xenonx.data.model.remote.update


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep
data class Versions (
    // if there is a problem in latest release , turn off  all update functionality
    // by turn off  "update_functionality" users never will be notified about update
    @SerializedName("update_functionality")
    var updateFunctionality: Boolean,
    //every user with VPN can update app
    @SerializedName("auto_update_enabled")
    var autoUpdateEnabled: Boolean,
    //just show update in settings screen with a red dot , and show in dashboard
    @SerializedName("show_indicated_update_v_LT")
    var indicatedUpdate: Int,
    //show optional update screen in startup
    @SerializedName("show_optional_dialog_v_LT")
    var optionalUpdate: Int,
    //force user to update app
    @SerializedName("minimum_available_version")
    var minimum_available_version: Int,
    //list of persian String about new features
    @SerializedName("update_features")
    var updateFeatures: List<String>,
    //if there is a problem with google play , user can download app from this link
    @SerializedName("download_link")
    var downloadLink: String,
)