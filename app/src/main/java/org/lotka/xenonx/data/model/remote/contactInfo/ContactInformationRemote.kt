package org.lotka.xenonx.data.model.remote.contactInfo


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.lotka.xenonx.data.base.ResponseObject
import org.lotka.xenonx.domain.model.model.contactInfo.ContactInformation

@Keep
data class ContactInformationRemote(
    @SerializedName("callNumber") var callNumber: String?,
    @SerializedName("departmentId") var departmentId: Int?,
    @SerializedName("departmentLogoUrl") var departmentLogoUrl: String?,
    @SerializedName("departmentName") var departmentName: String?,
    @SerializedName("fullName") var fullName: String?,
    @SerializedName("type") var type: String?,
    @SerializedName("whatsappAvailibility") var whatsappAvailibility: Boolean?
) : ResponseObject<ContactInformation> {
    override fun toDomain(): ContactInformation {
        return ContactInformation(
            callNumber = callNumber,
            departmentId = departmentId,
            departmentLogoUrl = departmentLogoUrl,
            departmentName = departmentName,
            fullName = fullName,
            type = type,
            whatsappAvailibility = whatsappAvailibility
        )
    }


}