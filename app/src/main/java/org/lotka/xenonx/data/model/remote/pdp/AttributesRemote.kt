package org.lotka.xenonx.data.model.remote.pdp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class AttributesRemote(
    @SerializedName("age") var age: Int?,
    @SerializedName("expired") var expired: Boolean?,
    @SerializedName("featured") var featured: Boolean?,
    @SerializedName("floorArea") var floorArea: Double?,
    @SerializedName("floorAreaUnit") var floorAreaUnit: String?,
    @SerializedName("hasVr") var hasVr: Boolean?,
    @SerializedName("landuseType") var landuseType: String?,
    @SerializedName("numOfBeds") var numOfBeds: Int?,
    @SerializedName("numOfParkings") var numOfParkings: Int?,
    @SerializedName("propertyType") var propertyType: String?,
    @SerializedName("showPrice") var showPrice: Boolean?,
    @SerializedName("specifics") var specifics: List<SpecificRemote?>?,
    @SerializedName("verified") var verified: Boolean?,
    @SerializedName("vrLink") var vrLink: Any?
)