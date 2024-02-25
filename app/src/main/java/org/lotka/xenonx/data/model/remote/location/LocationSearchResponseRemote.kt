package org.lotka.xenonx.data.model.remote.location


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import org.lotka.xenonx.data.base.ResponseObject
import org.lotka.xenonx.domain.model.model.location.LocationSearchModel

@Keep

data class LocationSearchResponseRemote(
    @SerializedName("results") var results: List<LocationSearchItemRemote?>?,
    @SerializedName("size") var size: Int?
) : ResponseObject<LocationSearchModel> {
    override fun toDomain(): LocationSearchModel {
        return LocationSearchModel(
            results = results?.map { it?.toDomain() },
            size = size
        )
    }
}