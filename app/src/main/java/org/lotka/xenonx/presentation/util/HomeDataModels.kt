package org.lotka.xenonx.presentation.util

import com.google.gson.annotations.SerializedName


data class HomeType(
    @SerializedName("listingType")
    val listingType: String?,
    @SerializedName("landUseTypes")
    val landUseTypes: String?,
    @SerializedName("homeUseTypes")
    val homeUseTypes: String?
)


data class HomeProperty(
    @SerializedName("meterage")
    val meterage: String?,
    @SerializedName("pricePerMeter")
    val pricePerMeter: String?,
    @SerializedName("totalPrice")
    val totalPrice: String?,
    @SerializedName("deposit")
    val deposit: String?,
    @SerializedName("rent")
    val rent: String?,
    @SerializedName("homeAge")
    val homeAge: String?,
    @SerializedName("numOfBedroom")
    val numOfBedroom: String?,
    @SerializedName("numOfParking")
    val numOfParking: String?,
    @SerializedName("agreedPrice")
    val agreedPrice: Boolean?,
    @SerializedName("fullyMortgage")
    val fullyMortgage: Boolean?,
    @SerializedName("newBuilt")
    val newBuilt: Boolean?
)


data class HomeCondition(
    @SerializedName("loanMount")
    val loanMount: String?,
    @SerializedName("unitShareMount")
    val unitShareMount: String?,
    @SerializedName("checkedConditions")
    val checkedConditions: String?,
    @SerializedName("checkedFacilities")
    val checkedFacilities: String?,
)


data class HomeAddress(
    @SerializedName("cityId")
    val cityId: String?,
    @SerializedName("cityName")
    val cityName: String?,
    @SerializedName("exactNameLocal")
    val exactNameLocal: String?,
    @SerializedName("phraseToShow")
    val phraseToShow: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("locationId")
    val locationId: String?,
    @SerializedName("mainRoad")
    val mainRoad: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("long")
    val long: String?
)


data class HomeDescription(
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("agentId")
    val agentId: String?,
    @SerializedName("isAdmin")
    val isAdmin: Boolean,
    @SerializedName("agentFirstName")
    val agentFirstName: String?,
    @SerializedName("agentLastName")
    val agentLastName: String?,
)

