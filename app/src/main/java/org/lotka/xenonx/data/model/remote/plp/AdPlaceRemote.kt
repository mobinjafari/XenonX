package org.lotka.xenonx.data.model.remote.plp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class AdPlaceRemote(
    @SerializedName("index") var index: Int?,
    @SerializedName("type") var type: String?
)