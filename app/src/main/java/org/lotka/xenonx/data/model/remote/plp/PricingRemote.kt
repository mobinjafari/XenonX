package org.lotka.xenonx.data.model.remote.plp


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class PricingRemote(
    @SerializedName("agreedPrice") var agreedPrice: Boolean?,
    @SerializedName("currency") var currency: String?,
    @SerializedName("deposit") var deposit: Long?,
    @SerializedName("depositOnly") var depositOnly: Boolean?,
    @SerializedName("price") var price: Long?,
    @SerializedName("rent") var rent: Long?,
    @SerializedName("unitPrice") var unitPrice: Long?
)