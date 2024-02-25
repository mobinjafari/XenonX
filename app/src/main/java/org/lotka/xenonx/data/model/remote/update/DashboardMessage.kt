package org.lotka.xenonx.data.model.remote.update


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep

data class DashboardMessage(
    @SerializedName("body")
    var body: String,
    @SerializedName("buttonText")
    var buttonText: String,
    @SerializedName("buttonUrl")
    var buttonUrl: String,
    @SerializedName("dismissible")
    var dismissible: Boolean,
    @SerializedName("enabled")
    var enabled: Boolean,
    @SerializedName("image")
    var image: String,
    @SerializedName("isURL")
    var isURL: Boolean,
    @SerializedName("secondText")
    var secondText: String,
    @SerializedName("title")
    var title: String
)