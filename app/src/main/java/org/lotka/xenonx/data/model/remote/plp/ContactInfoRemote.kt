package org.lotka.xenonx.data.model.remote.plp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class ContactInfoRemote(
    @SerializedName("agentId") var agentId: Int?,
    @SerializedName("logoUrl") var logoUrl: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("type") var type: String?
)