package org.lotka.xenonx.presentation.ui.app

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.ui.screens.pdp.PdpScreen
import org.lotka.xenonx.presentation.ui.screens.plp.PlpScreen
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
import org.lotka.xenonx.presentation.ui.screens.splash.SplashScreen
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
                    route = HomeScreensNavigation.splash.route,
                    ) {
                    SplashScreen(navController = navController)
                }




                composable(
                    route = HomeScreensNavigation.plp.route,
                ) {
                    val factory = HiltViewModelFactory(LocalContext.current, it)
                    val viewModel: PlpViewModel =
                        viewModel(activity, "RecipeDetailViewModel", factory)
                    PlpScreen(
                        navController = navController,
                        onNavigateToRecipeDetailScreen = navController::navigate,
                        viewModel = viewModel,
                        isDarkTheme = settingsDataStore.isDark.value,
                        onToggleTheme = settingsDataStore::toggleTheme,
                    )
                }

                composable(
                    route = HomeScreensNavigation.pdp.route + "/{recipeId}",
                    arguments = listOf(navArgument("recipeId") {
                        type = NavType.IntType
                    })
                ) {
                    PdpScreen(
                        navController = navController,
                        recipeId = navBackStackEntry?.arguments?.getInt("recipeId"),
                        isDarkTheme = settingsDataStore.isDark.value,
                        onToggleTheme = settingsDataStore::toggleTheme
                    )
                }


            }

        },
    )

}



