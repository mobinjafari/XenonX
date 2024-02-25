package org.lotka.xenonx.presentation.ui.navigation

sealed class HomeScreensNavigation(val route: String) {


    object splash : HomeScreensNavigation(route = "splash")

    object plp : HomeScreensNavigation(route = "plp")
    object pdp : HomeScreensNavigation(route = "pdp")

}