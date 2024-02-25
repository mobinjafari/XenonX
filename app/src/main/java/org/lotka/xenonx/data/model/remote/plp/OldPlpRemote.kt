package org.lotka.xenonx.data.model.remote.plp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import org.lotka.xenonx.data.base.ResponseObject
import org.lotka.xenonx.domain.enums.LandUseTypes
import org.lotka.xenonx.domain.enums.ListingType
import org.lotka.xenonx.domain.model.model.plp.PlpItemResultModel
import org.lotka.xenonx.domain.model.model.plp.PlpResponseModel

@Keep

data class OldPlpRemote(
    @SerializedName("content") var content: List<ContentRemote?>?,
    @SerializedName("totalListings") var totalListings: Int?
): ResponseObject<PlpResponseModel> {
    override fun toDomain(): PlpResponseModel {
        try {
            return PlpResponseModel(
                plpItemResultModel = content?.map { res ->

                    val listingType : ListingType =  ListingType.getByNameEn(res?.attributes?.listingType.toString())?:ListingType.SALE_AND_PRE_SALE
                    val landUseTypes : LandUseTypes = LandUseTypes.getLandUseTypeByNameFa(res?.attributes?.landuseType.toString())?:LandUseTypes.RESIDENTIAL

                    PlpItemResultModel(
                        agencyName = res?.contactInfo?.name,
                        agencyLogoUrl  = res?.contactInfo?.logoUrl,
                        featured = res?.attributes?.featured,
                        floorArea = res?.attributes?.floorArea?.toInt(),
                        id = res?.identifier?.toInt()?:0,
                        landUseType = landUseTypes,
                        lastModifiedDate = res?.searchDate?.toString()?:"",
                        listingType = listingType,
                        numBeds = res?.attributes?.numOfBeds,
                        numParkings = res?.attributes?.numberOfParking,
                        priceOrDeposit = if (listingType==ListingType.SALE_AND_PRE_SALE) res?.pricing?.price?.toLong() else res?.pricing?.deposit?.toLong(),
                        title = res?.title,
                        unitPriceOrRent =  if (listingType==ListingType.SALE_AND_PRE_SALE) res?.pricing?.unitPrice?.toLong() else res?.pricing?.rent?.toLong(),
                        agreedPrice = res?.pricing?.agreedPrice,
                        coverPicture = res?.media?.imageUrls?.firstOrNull(),
                        locationPhrase = res?.location?.locationName,
                        searchDate = res?.searchDate
                    )
                },
                total = totalListings?:0,
            )
        }catch (e: Exception){
            e.printStackTrace()
            return PlpResponseModel(
                plpItemResultModel = content?.map { res ->
                    PlpItemResultModel(
                        agencyName = null,
                        agencyLogoUrl  = null,
                        featured = null,
                        floorArea = null,
                        id =  0,
                        landUseType =  null,
                        lastModifiedDate = null,
                        listingType =  null,
                        numBeds =  null,
                        numParkings = null,
                        priceOrDeposit = null,
                        title =  null,
                        unitPriceOrRent = null,
                        agreedPrice =  null,
                        coverPicture =  null,
                        locationPhrase = null,
                        searchDate = null
                    )
                },
                total = totalListings?:0,
            )
        }

    }

}