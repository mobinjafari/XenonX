package org.lotka.xenonx.presentation.ui.navigation

sealed class HomeScreensNavigation(val route: String) {


    object splash : HomeScreensNavigation(route = "splash")

    object plp : HomeScreensNavigation(route = "plp")
    object pdp : HomeScreensNavigation(route = "pdp")

    object ChatHome : HomeScreensNavigation(route = "ChatHome")
    object RegisterScreen : HomeScreensNavigation(route = "Register")
    object LoginScreen : HomeScreensNavigation(route = "login")

}