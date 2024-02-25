package org.lotka.xenonx.domain.enums

import androidx.annotation.StringRes
import org.lotka.xenonx.R

sealed class AgencyReportType(
    val index: Int,
    @StringRes val resourceId: Int,
    val category: String
) {
    object AdvertisementType :
        AgencyReportType(index = 0, R.string.advertisement_type, "perListingType")

    object UserType : AgencyReportType(index = 1, R.string.user_type, "perLanduseType")
    object AdvertisementStatus :
        AgencyReportType(index = 2, R.string.advertisement_status, "perState")

    object AdvertisementWithPhoto :
        AgencyReportType(index = 3, R.string.advertisement_with_photo, "perHavePic")

    object RegisteredBy : AgencyReportType(index = 4, R.string.submitted_by, "perSource")
    object PersonalAdvertisement :
        AgencyReportType(index = 5, R.string.personal_advertisement, "OwnerListing")

    object OccasionAdvertisement :
        AgencyReportType(index = 6, R.string.occasion_advertisement, "Premium")
}