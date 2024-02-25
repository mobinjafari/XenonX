package org.lotka.xenonx.data.model.remote.pdp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class LocationRemote(
    @SerializedName("latitude") var latitude: Double?,
    @SerializedName("locationName") var locationName: String?,
    @SerializedName("longitude") var longitude: Double?,
    @SerializedName("targetGlobalLocation") var targetGlobalLocation: TargetGlobalLocationRemote?
)