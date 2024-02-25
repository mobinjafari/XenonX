package org.lotka.xenonx.data.model.remote.plp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class MediaRemote(
    @SerializedName("imageCount") var imageCount: Int?,
    @SerializedName("imageUrls") var imageUrls: List<String?>?
)