package org.lotka.xenonx.data.model.remote.update


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep

data class AppAvailablity(
    @SerializedName("body")
    var body: String,
    @SerializedName("buttonText")
    var buttonText: String,
    @SerializedName("buttonUrl")
    var buttonUrl: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("isAppAvailable")
    var isAppAvailable: Boolean,
    @SerializedName("secondText")
    var secondText: String,
    @SerializedName("title")
    var title: String
)