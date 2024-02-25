package org.lotka.xenonx.presentation.ui.screens.pdp

sealed class PdpScreenEvent {

    class ShowMessage(val message: String?) : PdpScreenEvent()
    object NavigateToPlpScreen : PdpScreenEvent()

    object NavigateToFilterAndSorting : PdpScreenEvent()

    object NavigateToEditAdvertisement : PdpScreenEvent()
    object NavigateToAddNewListing : PdpScreenEvent()

    class NavigateToLogin(val mobileNumber: String) : PdpScreenEvent()
    object NavigateToNoInternet : PdpScreenEvent()


    object NavigateToHomeTypeScreen : PdpScreenEvent()
    object NavigateToHomePropertyScreen : PdpScreenEvent()
    object NavigateToHomeConditionsScreen : PdpScreenEvent()
    object NavigateToAddImageScreen : PdpScreenEvent()
    object NavigateToAddressScreen : PdpScreenEvent()
    object NavigateToDescriptionViewScreen : PdpScreenEvent()

    data class GetRecipeEvent(
        val id: Int
    ): PdpScreenEvent()

    data class GetContactInformation(
        val id: Int
    ): PdpScreenEvent()


    data class SendTrackerEvent(
        val id: Int
    ): PdpScreenEvent()


}