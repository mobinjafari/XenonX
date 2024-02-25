package org.lotka.xenonx.data.model.remote.pdp


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.lotka.xenonx.data.base.ResponseObject
import org.lotka.xenonx.domain.enums.HomeFeaturesEnum
import org.lotka.xenonx.domain.enums.LandUseTypes
import org.lotka.xenonx.domain.enums.ListingType
import org.lotka.xenonx.domain.enums.getAvailableFeatures
import org.lotka.xenonx.domain.model.model.pdp.PdpModel

@Keep

data class PdpRemote(
    @SerializedName("attributes") var attributes: AttributesRemote?,
    @SerializedName("breadcrumbLinks") var breadcrumbLinks: List<BreadcrumbLinkRemote?>?,
    @SerializedName("description") var description: String?,
    @SerializedName("httpStatus") var httpStatus: Any?,
    @SerializedName("identifier") var identifier: String?,
    @SerializedName("isApproximate") var isApproximate: Boolean?,
    @SerializedName("listingType") var listingType: String?,
    @SerializedName("location") var location: LocationRemote?,
    @SerializedName("media") var media: List<MediaRemote?>?,
    @SerializedName("pricing") var pricing: PricingRemote?,
    @SerializedName("searchDate") var searchDate: String?,
    @SerializedName("seoDescription") var seoDescription: String?,
    @SerializedName("status") var status: Any?,
    @SerializedName("title") var title: String?,
    @SerializedName("type") var type: String?,
    @SerializedName("contactInfo") var contactInfo: ContactInfoRemote?
) : ResponseObject<PdpModel> {
    override fun toDomain(): PdpModel {

        //find listing type
        val listingType = when (listingType) {
            "buy" -> {
                ListingType.SALE_AND_PRE_SALE
            }
            "rent" -> {
                ListingType.MORTGAGE_AND_RENT
            }
            else -> {
                //if cant find set to sale/presale
                ListingType.SALE_AND_PRE_SALE
            }
        }


        //get homeFeatures
        val homeFeatures : MutableList<HomeFeaturesEnum>? = attributes?.specifics?.map { it?.type }?.let { getAvailableFeatures(it).toMutableList() }
        val isElevatorAvailable :  Boolean = homeFeatures?.contains(HomeFeaturesEnum.ELEVATOR)==true
        val isStorageAvailable :  Boolean = homeFeatures?.contains(HomeFeaturesEnum.STORAGE)==true
        //remove elevator from list
        homeFeatures?.remove(HomeFeaturesEnum.ELEVATOR)
        homeFeatures?.remove(HomeFeaturesEnum.STORAGE)


        return PdpModel(
            id = identifier?.toInt()?:-1,

            listingType =listingType ,
            landUseTypes = if(attributes?.landuseType=="مسکونی") LandUseTypes.RESIDENTIAL else LandUseTypes.COMMERCIAL,


            featured = false,

            largePictures = media?.map { it?.photoLargeUrl ?: ""  }?: emptyList(),
            smallPictures = media?.map { it?.photoSmallUrl ?: ""  }?: emptyList(),


            priceOrMortgage = if (listingType==ListingType.SALE_AND_PRE_SALE) pricing?.price?:-1L else pricing?.deposit?:-1L ,
            unitPriceOrRent = if (listingType==ListingType.SALE_AND_PRE_SALE) pricing?.unitPrice?:-1L else pricing?.rent?:-1L ,
            agreedPrice = pricing?.agreedPrice?: false,


            floorArea = attributes?.floorArea?.toInt()?:0,
            age = attributes?.age ?: 0,

            description =description ?: "",
            title = title ?: "",


            numberOfParking = attributes?.numOfParkings,
            numberOfRooms = attributes?.numOfBeds,
            isElevatorAvailable = isElevatorAvailable,
            isStorageAvailable = isStorageAvailable,
            homeFeatures = homeFeatures,
            locationPhrase=location?.locationName?:"-",
            mobileNumber ="09351699889",
            agencyLogo =contactInfo?.logoUrl,
            agentName = contactInfo?.name,
            fullyMortgage = pricing?.depositOnly



        )
    }
}