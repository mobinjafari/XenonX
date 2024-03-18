package org.lotka.xenonx.presentation.ui.screens.plp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import org.lotka.xenonx.domain.enums.FilterTypes
import org.lotka.xenonx.domain.model.model.location.LocationSearchItem
import org.lotka.xenonx.domain.model.model.plp.PlpItemResultModel
import org.lotka.xenonx.domain.usecase.location.SearchLocationUseCase
import org.lotka.xenonx.domain.usecase.plp.SearchPlpUseCase
import org.lotka.xenonx.domain.usecase.update.GetUpdateUseCase
import org.lotka.xenonx.domain.util.FilterManager
import org.lotka.xenonx.domain.util.ResultState
import org.lotka.xenonx.presentation.ui.app.BaseViewModel
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.PlpBottomSheetType
import org.lotka.xenonx.presentation.util.CustomUpdateManager
import org.lotka.xenonx.presentation.util.DialogQueue
import org.lotka.xenonx.presentation.util.DispatchersProvider
import org.lotka.xenonx.presentation.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import timber.log.Timber
import javax.inject.Inject


const val PAGE_SIZE = 23

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"


@HiltViewModel
class PlpViewModel @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val searchPlpUseCase: SearchPlpUseCase,
    private val searchLocationCase: SearchLocationUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val customUpdateManager: CustomUpdateManager,
    val updateUseCase: GetUpdateUseCase,

) : BaseViewModel(dispatchers) {
 




     var item: PlpItemResultModel? = null

    val filterManager = FilterManager()
    var isIndicatedUpdateAvailable by mutableStateOf(false)
    val searchAreaResult = MutableStateFlow<List<LocationSearchItem?>>(emptyList())
    val searchAreaUiState = MutableStateFlow<UIState>(UIState.Success)

    var filterStateVersion by mutableIntStateOf(0)

    val inChatProsess = mutableStateOf(false)

    private val _messages: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    // Exposed immutable state for observing
    val messages = _messages.asStateFlow()

    // Function to send a message
    fun sendMessage(message: String) {
        // Append the new message to the existing list of messages
        _messages.value += message
    }

    fun getMessageText(index: Int): String? {
        return messages.value.getOrNull(index)
    }


    private var initialFilterState: String = ""

    fun onHalfScreenBottomSheetOpened() {
        // Store the serialized version of the current filter state
        initialFilterState = serializeFilters(filterManager)
    }

    fun onFilterBottomSheetClosed() {
        // Compare the current filter state with the stored initial state
        val currentFilterState = serializeFilters(filterManager)
       if (currentFilterState != initialFilterState) {
            // Filters have effectively changed, trigger a new search
            filterStateVersion++
            onTriggerEvent(PlpScreenEvent.NewSearchEvent)
        }
        // If they are the same, do nothing
    }

    private fun serializeFilters(filterManager: FilterManager): String {
        // Implement serialization logic based on how your filters are structured
        // This could be a JSON string, a concatenated string of filter values, etc.
        return filterManager.getActiveFilters().toString() // Example placeholder
    }


    fun toggleCitySelection(area: LocationSearchItem) {
        filterManager.toggleAreaSelectionFilter(area)
        //update searchAreaResult to reflect the new isSelected state
        searchAreaResult.value.forEach { item ->
            item?.let {locationSearchItem->
                locationSearchItem.isSelected = filterManager.getSelectedLocations().mapNotNull { it.id }.toSet().contains(locationSearchItem.id)
            }
        }
        filterStateVersion++
    }

    fun addOrToggleFilter(key : FilterTypes, value: Any) {
        if (key == FilterTypes.LOCATION) {
            return
        }
        filterManager.setFilter(key, value)
        filterStateVersion++
    }

    fun removeFilter(key : FilterTypes){
        if (key == FilterTypes.LISTING_TYPE) {
            return
        }
        if (key == FilterTypes.LOCATION) {
            searchAreaResult.value.forEach { item ->
                item?.let {locationSearchItem->
                    locationSearchItem.isSelected = false
                }
            }
        }
        filterManager.removeFilter(key.serverKey)
        onTriggerEvent(PlpScreenEvent.NewSearchEvent)
        filterStateVersion++
    }

    private fun observeUpdateManager() {
        viewModelScope.launch {
            customUpdateManager.updateStatus.collectLatest{
                Timber.tag("updateLogger").d("updateStatus in main actvity: %s", it.toString())
                when(it){
                    CustomUpdateManager.UpdateType.INDICATED_UPDATE -> {
                        isIndicatedUpdateAvailable = true
                    }
                    else -> {}
                }
            }
        }

    }
    fun clearAllFilters(){
        //reset searchAreaResult to reflect the new isSelected state
        searchAreaResult.value.forEach { item ->
            item?.let {locationSearchItem->
                locationSearchItem.isSelected = false
            }
        }
        filterManager.clearAllFilters()
        onTriggerEvent(PlpScreenEvent.NewSearchEvent)
        filterStateVersion++
    }

    // StateFlow for holding the search query state
    private val _locationSearchQuery = MutableStateFlow("")
    val locationSearchQuery: StateFlow<String> = _locationSearchQuery.asStateFlow()

    // Function to update the search query
    fun setSearchQuery(query: String) {
        _locationSearchQuery.value = query
        viewModelScope.launch {
            searchAreaUiState.emit(UIState.Loading)
        }
    }

    fun clearSearchQuery() {
        _locationSearchQuery.value = ""
        viewModelScope.launch {
            searchAreaUiState.emit(UIState.Loading)
        }
    }

    fun performSearch() {
        viewModelScope.launch {
            onTriggerEvent(PlpScreenEvent.SearchLocationPhrase)
        }
    }


    val sessions = MutableStateFlow<List<PlpItemResultModel?>>(emptyList())
    val sessionUiState = MutableStateFlow<UIState>(UIState.Success)




    var halfScreenActiveBottomSheet by mutableStateOf(PlpBottomSheetType.NONE)
    var fullScreenActiveBottomSheet by mutableStateOf(PlpBottomSheetType.NONE)

    // Pagination starts at '1' (-1 = exhausted)
    val page = mutableIntStateOf(0)
    val latestIndex = mutableIntStateOf(0)
    private val totalItems = mutableIntStateOf(0)
    var savedScrollIndex by mutableIntStateOf(0)

    private var recipeListScrollPosition = 0

    val dialogQueue = DialogQueue()
    val isActiveJobRunning = mutableStateOf(false)


    init {
        viewModelScope.launch {
            updateUseCase.invoke().collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        Timber.tag("updateLogger").d("error: ${it.error.error}")
                        //could not connect to cdn or there is no internet connection
                    }
                    is ResultState.Loading -> {}
                    is ResultState.Success -> {
                        Timber.tag("updateLogger").d("Success: ${it.data.toString()}")
                        customUpdateManager.setAppVersionData(
                            updateFunctionality = it.data?.versions?.updateFunctionality ?: false,
                            autoUpdateEnabled = it.data?.versions?.autoUpdateEnabled ?: true,
                            indicatedUpdate = it.data?.versions?.indicatedUpdate ?: -1,
                            optionalUpdate = it.data?.versions?.optionalUpdate ?: -1,
                            forceUpdate = it.data?.versions?.minimum_available_version ?: -1,
                            downloadLink = it.data?.versions?.downloadLink ?: "",
                            updateFeatures = it.data?.versions?.updateFeatures ?: emptyList(),
                        )
                    }

                    else -> {}
                }
            }
        }
        observeUpdateManager()
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }

        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            setListScrollPosition(p)
        }


        if (recipeListScrollPosition != 0) {
            onTriggerEvent(PlpScreenEvent.RestoreStateEvent)
        } else {
            onTriggerEvent(PlpScreenEvent.NewSearchEvent)
        }


    }


    fun showBottomSheet(type: PlpBottomSheetType) {
        viewModelScope.launch{
            if(type==PlpBottomSheetType.LOCATION_SEARCH){
                fullScreenActiveBottomSheet=type
            }else{
                halfScreenActiveBottomSheet=(type)
            }
        }
    }


    private fun appendRecipes(recipes: List<PlpItemResultModel?>) {
        val recipes2 = recipes
        val current = ArrayList(this.sessions.value)
        current.addAll(recipes2)
        this.sessions.value = current
    }


    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun incrementPage() {
        setPage(page.value + 1)
    }

    private fun decrementPage() {
        setPage(page.value - 1)
    }

    private fun searchLocationPhrase() {
        viewModelScope.launch {
            searchAreaUiState.emit(UIState.Loading)
            searchLocationCase.invoke(
                text = _locationSearchQuery.value
            ).collect {
                when (it) {
                    is ResultState.Error -> {
                        Log.d("mobintest", "${it.error}: ")
                        isActiveJobRunning.value = true
                        searchAreaUiState.emit(UIState.Error(it.error.error?.detail.toString()))
                        isActiveJobRunning.value = false
                    }
                    is ResultState.Loading -> {
                        searchAreaUiState.emit(UIState.Loading)
                    }
                    is ResultState.Success -> {
                        val selectedAreaIds = filterManager.getSelectedLocations().mapNotNull { it?.id }.toSet()
                        val updatedList = it.data?.results?.map { item ->
                            if (item != null && item.id in selectedAreaIds) {
                                item.copy(isSelected = true)
                            } else {
                                item
                            }
                        }.orEmpty()
                        searchAreaUiState.emit(UIState.Success)
                        searchAreaResult.emit(updatedList)
                    }

                    else -> {}
                }
            }
        }
    }
    private fun newSearch() {
        page.intValue = 0
        viewModelScope.launch {
            sessionUiState.emit(UIState.Loading)
            searchPlpUseCase.invoke(
                page = page.intValue,
                filters = filterManager.getFiltersString()
            ).collect {
                when (it) {
                    is ResultState.Error -> {
                        isActiveJobRunning.value = true
                        sessionUiState.emit(UIState.Error(it.error.error?.msg.toString()))
                        dialogQueue.appendErrorMessage(it.error.error?.msg.toString(), "لطفا وضعیت اینترنت خود را بررسی کنید")
                        isActiveJobRunning.value = false
                    }
                    is ResultState.Loading -> {
                        sessionUiState.emit(UIState.Loading)
                    }
                    is ResultState.Success -> {
                        sessions.value = emptyList()
                        sessionUiState.emit(UIState.Success)
                        it.data?.plpItemResultModel?.let { it1 -> appendRecipes(recipes = it1) }
                        totalItems.value = it.data?.total ?: 0
                    }

                    else -> {}
                }
            }
        }
    }

    private fun nextPage() {
        isActiveJobRunning.value = true
        if (totalItems.value > (page.value * PAGE_SIZE)) {
            viewModelScope.launch {
                if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
                    incrementPage()
                    if (page.value >= 1) {
                        sessionUiState.emit(UIState.PaginationLoading)
                        searchPlpUseCase.invoke(
                            page = page.value,
                            filters = filterManager.getFiltersString()
                        ).collect {
                            when (it) {
                                is ResultState.Error -> {
                                    sessionUiState.emit(UIState.PaginationError)
                                    decrementPage()
                                    isActiveJobRunning.value = false
                                }
                                is ResultState.Loading -> {
                                    sessionUiState.emit(UIState.PaginationLoading)
                                    isActiveJobRunning.value = true
                                }
                                is ResultState.Success -> {
                                    it.data?.plpItemResultModel?.let { it1 -> appendRecipes(recipes = it1) }
                                    totalItems.value = it.data?.total ?: 0
                                    viewModelScope.launch {
                                        delay(1400)
                                        sessionUiState.emit(UIState.Success)
                                        isActiveJobRunning.value = false
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }


    }

    fun onTriggerEvent(event: PlpScreenEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is PlpScreenEvent.NewSearchEvent -> {
                        newSearch()
                    }
                    is PlpScreenEvent.NextPageEvent -> {
                        nextPage()
                    }
                    is PlpScreenEvent.SearchLocationPhrase -> {
                        searchLocationPhrase()
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
            }
        }
    }

    private fun setListScrollPosition(position: Int) {
        recipeListScrollPosition = position + 1
        savedStateHandle[STATE_KEY_LIST_POSITION] = position
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        setListScrollPosition(position = position)
    }










}


