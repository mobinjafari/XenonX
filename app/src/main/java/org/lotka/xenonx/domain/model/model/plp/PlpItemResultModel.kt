package org.lotka.xenonx.domain.model.model.plp


import androidx.annotation.Keep
import org.lotka.xenonx.domain.enums.LandUseTypes
import org.lotka.xenonx.domain.enums.ListingType

@Keep
data class PlpItemResultModel(
    var agencyName : String?,
    var agencyLogoUrl : String?,
    var featured: Boolean?,
    var floorArea: Int?,
    var id: Int,
    var landUseType: LandUseTypes?,
    var lastModifiedDate: String?,
    var listingType: ListingType?,
    var numBeds: Int?,
    var numParkings: Int?,
    var priceOrDeposit: Long?,
    var title: String?,
    var unitPriceOrRent: Long?,
    var agreedPrice: Boolean?,
    var coverPicture: String?,
    var searchDate : String?,
    var locationPhrase : String?,

    )