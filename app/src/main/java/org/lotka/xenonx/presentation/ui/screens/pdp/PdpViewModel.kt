package org.lotka.xenonx.presentation.ui.screens.pdp

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.lotka.xenonx.*
import org.lotka.xenonx.data.repository.*
import org.lotka.xenonx.domain.model.model.contactInfo.ContactInformation
import org.lotka.xenonx.domain.model.model.pdp.PdpModel
import org.lotka.xenonx.domain.usecase.pdp.GetUserContactUseCase
import org.lotka.xenonx.domain.usecase.pdp.PdpCountTrackerUseCase
import org.lotka.xenonx.domain.usecase.pdp.PdpGetDetailUseCase
import org.lotka.xenonx.domain.util.ResultState
import org.lotka.xenonx.presentation.ui.screens.pdp.bottom_sheet.PdpBottomSheetType
import org.lotka.xenonx.presentation.util.DialogQueue
import org.lotka.xenonx.presentation.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.*

const val STATE_KEY_RECIPE = "recipe.state.recipe.key"

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class PdpViewModel @Inject constructor(
    private val pdpGetDetailUseCase: PdpGetDetailUseCase,
    private val pdpCountTrackerUseCase: PdpCountTrackerUseCase,
    private val savedState: SavedStateHandle,
    private val getUserContactUseCase: GetUserContactUseCase
) : ViewModel() {

    val dialogQueue = DialogQueue()
    val isActiveJobRunning = mutableStateOf(false)
    var activeBottomSheet by mutableStateOf(PdpBottomSheetType.NONE)

    val pdpDetail = MutableStateFlow<PdpModel?>(null)
    val pdpDetailUiState = MutableStateFlow<UIState>(UIState.Loading)

    val contactInformation = MutableStateFlow<ContactInformation?>(null)
    val contactInformationUiState = MutableStateFlow<UIState>(UIState.Loading)

    val onLoad: MutableState<Boolean> = mutableStateOf(false)
    var features = mutableStateOf(listOf<String>())
    var conditions = mutableStateOf(mutableListOf<String>())
    fun showBottomSheet(type: PdpBottomSheetType) {
        activeBottomSheet = type
    }


    init {
        savedState.get<Int>(STATE_KEY_RECIPE)?.let { recipeId ->
            // onTriggerEvent(RecipeEvent.GetRecipeEvent(recipeId))
        }


    }


    private fun requestPdpDetail(id: Int) {
        viewModelScope.launch {
            pdpDetailUiState.emit(UIState.Loading)
            pdpGetDetailUseCase.invoke(id = id).collect {
                when (it) {
                    is ResultState.Error -> {
                        isActiveJobRunning.value = true
                        pdpDetailUiState.emit(UIState.Error(it.error.error?.msg.toString()))
                        dialogQueue.appendErrorMessage(
                            "An Error Occurred", it.error.error?.msg.toString()
                        )
                        pdpDetailUiState.emit(UIState.Error(it.error.error?.msg.toString()))
                        isActiveJobRunning.value = false
                        if (it.error.error?.code == 404.toString()) {
                            onTriggerEvent(PdpScreenEvent.NavigateToPlpScreen)
                        }
                        onLoad.value = true
                    }

                    is ResultState.Loading -> {
                        isActiveJobRunning.value = true
                        pdpDetailUiState.emit(UIState.Loading)
                        pdpDetailUiState.emit(UIState.Loading)
                    }

                    is ResultState.Success -> {
                        isActiveJobRunning.value = false
                        pdpDetail.value = it.data
                        pdpDetailUiState.emit(UIState.Success)
                    }
                }
            }



        }

    }
    private fun requestPdpTracker(id: Int) {
        viewModelScope.launch {
            pdpCountTrackerUseCase.invoke(id = id).collect {
                when (it) {
                    is ResultState.Error -> {
                        requestPdpTracker(id)
                    }

                    is ResultState.Loading -> {
                    }

                    is ResultState.Success -> {

                    }
                }
            }



        }

    }



    private fun requestContactInformation(id: Int) {
        viewModelScope.launch {
            contactInformationUiState.emit(UIState.Loading)
            getUserContactUseCase.invoke(id = id).collect {
                when (it) {
                    is ResultState.Error -> {
                        isActiveJobRunning.value = true
                        contactInformationUiState.emit(UIState.Error(it.error.error?.msg.toString()))
                        dialogQueue.appendErrorMessage(
                            "An Error Occurred", it.error.error?.msg.toString()
                        )
                        isActiveJobRunning.value = false
                        if (it.error.error?.code == 404.toString()) {
                            onTriggerEvent(PdpScreenEvent.NavigateToPlpScreen)
                        }
                        onLoad.value = true
                        viewModelScope.launch {
                            delay(3000)
                            requestContactInformation(id)
                        }

                    }

                    is ResultState.Loading -> {
                        isActiveJobRunning.value = true
                        contactInformationUiState.emit(UIState.Loading)
                    }

                    is ResultState.Success -> {
                        isActiveJobRunning.value = false
                        contactInformation.value = it.data
                        contactInformationUiState.emit(UIState.Success)
                    }
                }
            }
        }

    }

    fun onTriggerEvent(event: PdpScreenEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is PdpScreenEvent.GetRecipeEvent -> {
                        requestPdpDetail(event.id)
                    }

                    is PdpScreenEvent.GetContactInformation -> {
                        requestContactInformation(event.id)
                    }

                    is PdpScreenEvent.SendTrackerEvent -> {
                        requestPdpTracker(event.id)
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var loading by mutableStateOf(true)
    var selectedImageIndex by mutableIntStateOf(-1)

}