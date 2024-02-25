package org.lotka.xenonx.data.model


import com.google.gson.annotations.SerializedName
import org.lotka.xenonx.domain.util.HttpErrors

data class ErrorMessage2(
    @SerializedName("data")
    var `data`: String? = "",
    @SerializedName("error")
    var error: ErrorDetail? = ErrorDetail("", "", ""),
    @SerializedName("traceId")
    var traceId: String? = "",
    val status: HttpErrors,
)