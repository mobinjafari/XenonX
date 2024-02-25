package org.lotka.xenonx.data.model.remote.update


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep

data class StartupMessage(
    @SerializedName("body")
    var body: String,
    @SerializedName("buttonText")
    var buttonText: String,
    @SerializedName("buttonUrl")
    var buttonUrl: String,
    @SerializedName("closeable")
    var closeable: Boolean,
    @SerializedName("enabled")
    var enabled: Boolean,
    @SerializedName("image")
    var image: String,
    @SerializedName("secondText")
    var secondText: String,
    @SerializedName("title")
    var title: String
)