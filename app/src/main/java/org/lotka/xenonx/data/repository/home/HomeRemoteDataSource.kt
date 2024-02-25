package org.lotka.xenonx.data.repository.home


import org.lotka.xenonx.data.api.CdnService
import org.lotka.xenonx.data.api.HomeService
import org.lotka.xenonx.data.exceptions.NetworkExceptionHandler
import org.lotka.xenonx.domain.model.model.contactInfo.ContactInformation
import org.lotka.xenonx.domain.model.model.location.LocationSearchModel
import org.lotka.xenonx.domain.model.model.pdp.PdpModel
import org.lotka.xenonx.domain.model.model.plp.PlpResponseModel
import org.lotka.xenonx.domain.model.model.update.AppStatusResponse
import org.lotka.xenonx.domain.util.ResultState
import org.lotka.xenonx.domain.util.Sektorum
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRemoteDataSource @Inject constructor(
    @Sektorum private val apiService: HomeService,
     private val cdnService: CdnService,
    private val apiExceptionHandler: NetworkExceptionHandler,
) : HomeDataSource {
    override suspend fun searchLocation(text: String): ResultState<LocationSearchModel> {
        return try {
            val result = apiService.fuzzySearchCountry(phrase = text)
            ResultState.Success(result.toDomain())
        } catch (e: Exception) {
            ResultState.Error(apiExceptionHandler.traceErrorException(e))
        }
    }



    override suspend fun pdpDetail(id: Int): ResultState<PdpModel> {
        return try {
            val result = apiService.getSingleListing(identifier = id)
            ResultState.Success(result.toDomain())
        } catch (e: Exception) {
            ResultState.Error(apiExceptionHandler.traceErrorException(e))
        }
    }

    override suspend fun pdpCountTracker(id: Int): ResultState<Boolean> {
        return try {
            val result = apiService.pdpCountTracker(identifier = id)
            ResultState.Success(result)
        } catch (e: Exception) {
            ResultState.Error(apiExceptionHandler.traceErrorException(e))
        }
    }


    override suspend fun loadPlpList(
        page: Int,
        filters: String
    ): ResultState<PlpResponseModel> {
        return try {

          //  val jsonFilters = JSONObject(Gson().toJson(filters))
            val mediaType = "application/json; charset=utf-8".toMediaType()
           // val requestBody = jsonFilters.toRequestBody(mediaType)
            val result = apiService.searchListings(page = page, requestBody = filters.toString().toRequestBody(mediaType))
            ResultState.Success(result.toDomain())
        } catch (e: Exception) {
            ResultState.Error(apiExceptionHandler.traceErrorException(e))
        }
    }

    override suspend fun getContactInformation(identifier: Int): ResultState<ContactInformation> {
        return try {
            val result = apiService.getContactInfo(identifier = identifier)
            ResultState.Success(result.toDomain())
        } catch (e: Exception) {
            ResultState.Error(apiExceptionHandler.traceErrorException(e))
        }
    }

    override suspend fun getAppStatus(): ResultState<AppStatusResponse> {
        return try {
            val result = cdnService.checkAppStatus()
            ResultState.Success(result.toDomain())
        } catch (e: Exception) {
            ResultState.Error(apiExceptionHandler.traceErrorException(e))
        }
    }


}