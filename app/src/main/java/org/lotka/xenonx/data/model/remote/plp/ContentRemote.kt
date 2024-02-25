package org.lotka.xenonx.data.model.remote.plp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class ContentRemote(
    @SerializedName("attributes") var attributes: AttributesRemote?,
    @SerializedName("contactInfo") var contactInfo: ContactInfoRemote?,
    @SerializedName("identifier") var identifier: String?,
    @SerializedName("location") var location: LocationRemote?,
    @SerializedName("media") var media: MediaRemote?,
    @SerializedName("organizationId") var organizationId: Int?,
    @SerializedName("pricing") var pricing: PricingRemote?,
    @SerializedName("searchDate") var searchDate: String?,
    @SerializedName("title") var title: String?,
    @SerializedName("type") var type: String?
)