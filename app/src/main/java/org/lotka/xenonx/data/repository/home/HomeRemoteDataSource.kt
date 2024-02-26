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
import org.lotka.xenonx.domain.model.model.plp.PlpItemResultModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

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
    ): ResultState<PlpResponseModel> {

        val allItems = List(10000) { index ->
            // Calculate image number within the range 3000 to 4000
            val imageNumber = 3000 + (index % 1001) // Adjusted to cycle through 3000 to 4000 correctly
            PlpItemResultModel(
                id = index,
                userFirstName = "User$index",
                userLastName = "Last$index",
                smallProfileImage = "https://ozgrozer.github.io/100k-faces/0/3/00${imageNumber}.jpg",
                lastMessageText = "Hello from User$index",
                numUnreadMessage = index % 3,
                lastTypingDate = "10:01",
                isPremiumUser = index % 2 == 0,
                lastMessageType = "text",
                lastChatSeenDate = System.currentTimeMillis() - (1000 * 60 * 60 * 24 * index),
                lastMessageStatus = "sent",
                hasStory = index % 2 == 1,
                lastMessageDate = "15:00",
            )
        }

        // Assuming each page shows 20 items
        val pageSize = 23
        // Adjust calculation for startIndex to accommodate page starting at 0
        val startIndex = page * pageSize
        // Calculate the endIndex for the given page
        val endIndex = min(startIndex + pageSize, allItems.size)

        return try {
            // Check if the page number is within the valid range
            if (startIndex < allItems.size) {
                // Extract the sublist for the requested page
                val pageItems = allItems.subList(startIndex, endIndex)
                // Assuming PlpResponseModel can directly take the sublist of items
                val responseModel = PlpResponseModel(pageItems, 10000)
                ResultState.Success(responseModel)
            } else {
                // Handle case where the page number is out of range
                ResultState.Error(apiExceptionHandler.traceErrorException(Throwable("Page number out of range")))
            }
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