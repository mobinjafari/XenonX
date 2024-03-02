package org.lotka.xenonx.presentation.ui.navigation

sealed class HomeScreensNavigation(val route: String) {


    object splash : HomeScreensNavigation(route = "splash")

    object plp : HomeScreensNavigation(route = "plp")
    object pdp : HomeScreensNavigation(route = "pdp")

    object HomeChatScreen : HomeScreensNavigation(route = "HomeChatScreen")
    object RegisterScreen : HomeScreensNavigation(route = "Register")
    object LoginScreen : HomeScreensNavigation(route = "login")
    object Setting : HomeScreensNavigation(route = "setting")
    object ProfileScreen : HomeScreensNavigation(route = "profileScreen")
    object SingleChat : HomeScreensNavigation(route = "singleChat")


}