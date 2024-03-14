package com.kilid.portal.presentation.ui.app

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.pager.ExperimentalPagerApi




import org.lotka.xenonx.presentation.ui.app.HomeActivity
import org.lotka.xenonx.presentation.ui.app.MainViewModel
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.ui.screens.plp.PlpScreen


import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
import org.lotka.xenonx.presentation.ui.screens.plp.SingleChatScreen.SingleChatScreen

import org.lotka.xenonx.util.SettingsDataStore


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeApp(
    activity: HomeActivity,
    viewModel: MainViewModel,
    navController: NavHostController,
    settingsDataStore: SettingsDataStore,
    plpviewModel: PlpViewModel,
    onNavigateToRecipeDetailScreen: (String) -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,

) {


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scaffoldState = rememberScaffoldState()




    Scaffold(

        content = { _ ->
            NavHost(navController = navController,
                startDestination = HomeScreensNavigation.plp.route,
                enterTransition = {
                    // you can change whatever you want transition
                    EnterTransition.None
                },
                exitTransition = {
                    // you can change whatever you want transition
                    ExitTransition.None
                }) {
                composable(
                    route = HomeScreensNavigation.plp.route,
                ) {
                    PlpScreen(
                        navController = navController
                    ,   onToggleTheme = onToggleTheme,
                        onNavigateToRecipeDetailScreen = onNavigateToRecipeDetailScreen,
                        isDarkTheme = isDarkTheme,
                        viewModel = plpviewModel,


                    )
                }
                composable(
                    route = HomeScreensNavigation.SingleChatScreen.route,
                ) {
                    SingleChatScreen(
                      onBackPressed = { navController.popBackStack() },
                        isDarkMode = isDarkTheme,
                        onToggleTheme = onToggleTheme,
                        viewModel = plpviewModel,
                        navController = navController,




                    )
                }




//                composable(
//                    route = HomeScreensNavigation.RegisterScreen.route,
//                ) {
//                    RegisterScreen(navController = navController)
//                }


//                composable(
//                    route = HomeScreensNavigation.LoginScreen.route,
//                ) {
//                    LoginScreen(navController = navController)
//                }









            }

        },
    )

}



