package org.lotka.xenonx.data.model.remote.pdp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class ContactInfoRemote(
    @SerializedName("logoUrl") var logoUrl: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("agentId") var agentId: Long?,
    @SerializedName("agentName") var agentName: String?,
)