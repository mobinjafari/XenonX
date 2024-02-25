package org.lotka.xenonx.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ErrorDetail(
    @SerializedName("code")
    var code: String = "",
    @SerializedName("detail")
    var detail: String? = "",
    @SerializedName("msg")
    var msg: String? = ""
) : Parcelable