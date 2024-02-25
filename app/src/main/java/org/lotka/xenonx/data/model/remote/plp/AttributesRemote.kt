package org.lotka.xenonx.data.model.remote.plp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class AttributesRemote(
    @SerializedName("expired") var expired: Boolean?,
    @SerializedName("featured") var featured: Boolean?,
    @SerializedName("floorArea") var floorArea: Double?,
    @SerializedName("floorAreaUnit") var floorAreaUnit: String?,
    @SerializedName("hasVr") var hasVr: Boolean?,
    @SerializedName("landuseType") var landuseType: String?,
    @SerializedName("listingType") var listingType: String?,
    @SerializedName("numOfBeds") var numOfBeds: Int?,
    @SerializedName("numberOfParking") var numberOfParking: Int?,
    @SerializedName("showPrice") var showPrice: Boolean?,
    @SerializedName("verified") var verified: Boolean?
)