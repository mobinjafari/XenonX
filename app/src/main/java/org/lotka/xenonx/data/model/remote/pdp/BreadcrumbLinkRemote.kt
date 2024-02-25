package org.lotka.xenonx.data.model.remote.pdp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class BreadcrumbLinkRemote(
    @SerializedName("index") var index: Int?,
    @SerializedName("linkUrl") var linkUrl: String?,
    @SerializedName("name") var name: String?
)