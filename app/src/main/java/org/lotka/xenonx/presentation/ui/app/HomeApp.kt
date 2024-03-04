package com.kilid.portal.presentation.ui.app

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.chatwithme.presentation.bottomnavigation.BottomNavItem
import com.example.chatwithme.presentation.chat.ChatScreen
import com.example.chatwithme.presentation.profile.ProfileScreen
import com.example.chatwithme.presentation.userlist.Userlist
import com.google.accompanist.pager.ExperimentalPagerApi
import org.lotka.xenonx.presentation.ui.app.HomeActivity
import org.lotka.xenonx.presentation.ui.app.MainViewModel
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
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
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController

) {


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scaffoldState = rememberScaffoldState()




    Scaffold(

        content = { _ ->
            NavHost(navController = navController,
                startDestination = HomeScreensNavigation.HomeChatScreen.route,
                enterTransition = {
                    // you can change whatever you want transition
                    EnterTransition.None
                },
                exitTransition = {
                    // you can change whatever you want transition
                    ExitTransition.None
                }) {
                composable(
                    BottomNavItem.Profile.fullRoute,
                    enterTransition = {
                        when (initialState.destination.route) {
                            BottomNavItem.UserList.fullRoute ->
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )

                            else -> null
                        }

                    }, exitTransition = {
                        when (targetState.destination.route) {
                            BottomNavItem.UserList.fullRoute ->
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )

                            else -> null
                        }
                    }) {
                    ProfileScreen(
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        keyboardController = keyboardController
                    )
                }
                composable(
                    BottomNavItem.UserList.fullRoute,
                    enterTransition = {
                        when (initialState.destination.route) {
                            BottomNavItem.Profile.fullRoute ->
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )
//                    BottomNavItem.SignIn.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
//                    BottomNavItem.SignUp.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))
//                    BottomNavItem.Profile.fullRoute ->
//                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700))

                            else -> null
                        }

                    }, exitTransition = {
                        when (targetState.destination.route) {
                            BottomNavItem.UserList.fullRoute ->
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )

                            else -> null
                        }
                    }) {
                    Userlist(
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        keyboardController = keyboardController
                    )
                }

                composable(
                    BottomNavItem.Chat.fullRoute,
                    arguments = listOf(
                        navArgument("chatroomUUID") {
                            type = NavType.StringType
                        }, navArgument("opponentUUID") {
                            type = NavType.StringType
                        }, navArgument("registerUUID") {
                            type = NavType.StringType
                        }, navArgument("oneSignalUserId") {
                            type = NavType.StringType
                        }),
                    enterTransition = {
                        when (initialState.destination.route) {
//                    BottomNavItem.UserList.fullRoute -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
                            else -> null
                        }

                    }, exitTransition = {

                        when (targetState.destination.route) {
//                    BottomNavItem.UserList.fullRoute -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))
                            else -> null
                        }
                    }) {

                    val chatroomUUID = remember {
                        it.arguments?.getString("chatroomUUID")
                    }
                    val opponentUUID = remember {
                        it.arguments?.getString("opponentUUID")
                    }
                    val registerUUID = remember {
                        it.arguments?.getString("registerUUID")
                    }
                    val oneSignalUserId = remember {
                        it.arguments?.getString("oneSignalUserId")
                    }

                    ChatScreen(
                        chatRoomUUID = chatroomUUID ?: "",
                        opponentUUID = opponentUUID ?: "",
                        registerUUID = registerUUID ?: "",
                        oneSignalUserId = oneSignalUserId ?: "",
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        keyboardController = keyboardController
                    )


                }
            }

        }   )}




