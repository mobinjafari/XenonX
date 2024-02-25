package org.lotka.xenonx.data.model.remote.pdp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class MediaRemote(
    @SerializedName("isCover") var isCover: Boolean?,
    @SerializedName("mediaUrl") var mediaUrl: Any?,
    @SerializedName("photoLargeUrl") var photoLargeUrl: String?,
    @SerializedName("photoMediumUrl") var photoMediumUrl: Any?,
    @SerializedName("photoSmallUrl") var photoSmallUrl: String?,
    @SerializedName("type") var type: String?
)