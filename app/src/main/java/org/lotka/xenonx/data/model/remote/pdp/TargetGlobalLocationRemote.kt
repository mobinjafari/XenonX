package org.lotka.xenonx.data.model.remote.pdp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class TargetGlobalLocationRemote(
    @SerializedName("id") var id: Int?,
    @SerializedName("type") var type: String?
)