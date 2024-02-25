package org.lotka.xenonx.data.model.remote.pdp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class SpecificRemote(
    @SerializedName("name") var name: String?,
    @SerializedName("type") var type: String?
)