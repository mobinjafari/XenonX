package org.lotka.xenonx.data.model.remote.location


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import org.lotka.xenonx.data.base.ResponseObject
import org.lotka.xenonx.domain.model.model.location.LocationSearchItem

@Keep

data class LocationSearchItemRemote(
    @SerializedName("cityId") var cityId: Long?,
    @SerializedName("cityName") var cityName: String?,
    @SerializedName("exactNameLat") var exactNameLat: String?,
    @SerializedName("exactNameLocal") var exactNameLocal: String?,
    @SerializedName("geographicType") var geographicType: String?,
    @SerializedName("globalLocationId") var globalLocationId: Long?,
    @SerializedName("id") var id: Long?,
    @SerializedName("locationId") var locationId: Long?,
    @SerializedName("locationTypeId") var locationTypeId: Long?,
    @SerializedName("phraseToShow") var phraseToShow: String?,
    @SerializedName("score") var score: Double?,
    @SerializedName("type") var type: String?,
    @SerializedName("useForLegacyService") var useForLegacyService: Boolean?
) : ResponseObject<LocationSearchItem> {
    override fun toDomain(): LocationSearchItem {
        return LocationSearchItem(
            cityId = cityId,
            cityName = cityName,
            exactNameLat = exactNameLat,
            exactNameLocal = exactNameLocal,
            geographicType = geographicType,
            globalLocationId = globalLocationId,
            id = id,
            locationId = locationId,
            locationTypeId = locationTypeId,
            phraseToShow = phraseToShow,
            score = score,
            type = type,
            useForLegacyService = useForLegacyService
        )
    }
}