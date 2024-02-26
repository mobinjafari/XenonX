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

import org.lotka.xenonx.presentation.ui.screens.chats.HomeTabRow
import com.kilid.portal.presentation.ui.screens.chats.LoginScreen
import com.kilid.portal.presentation.ui.screens.chats.RegisterScreen



import org.lotka.xenonx.presentation.ui.app.HomeActivity
import org.lotka.xenonx.presentation.ui.app.MainViewModel
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.util.SettingsDataStore


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeApp(
    activity: HomeActivity,
    viewModel: MainViewModel,
    navController: NavHostController,
    settingsDataStore: SettingsDataStore
) {


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scaffoldState = rememberScaffoldState()




    Scaffold(

        content = { _ ->
            NavHost(navController = navController,
                startDestination = HomeScreensNavigation.RegisterScreen.route,
                enterTransition = {
                    // you can change whatever you want transition
                    EnterTransition.None
                },
                exitTransition = {
                    // you can change whatever you want transition
                    ExitTransition.None
                }) {

                composable(
                    route = HomeScreensNavigation.RegisterScreen.route,
                ) {
                    RegisterScreen(navController = navController)
                }


                composable(
                    route = HomeScreensNavigation.LoginScreen.route,
                ) {
                    LoginScreen(navController = navController)
                }


                composable(
                    route = HomeScreensNavigation.ChatHome.route,
                ) {
                    HomeTabRow(navController = navController)
                }





            }

        },
    )

}



