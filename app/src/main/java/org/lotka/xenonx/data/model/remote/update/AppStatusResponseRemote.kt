package org.lotka.xenonx.data.model.remote.update


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep
import org.lotka.xenonx.data.base.ResponseObject
import org.lotka.xenonx.domain.model.model.update.AppStatusResponse

@Keep

data class AppStatusResponseRemote(
    @SerializedName("app_availablity")
    var appAvailablity: AppAvailablity,
    @SerializedName("dashboard_message")
    var dashboardMessage: DashboardMessage,
    @SerializedName("startup_message")
    var startupMessage: StartupMessage,
    @SerializedName("versions")
    var versions: Versions
): ResponseObject<AppStatusResponse> {
    override fun toDomain(): AppStatusResponse {
        return AppStatusResponse(
            versions = null
        )
    }
}