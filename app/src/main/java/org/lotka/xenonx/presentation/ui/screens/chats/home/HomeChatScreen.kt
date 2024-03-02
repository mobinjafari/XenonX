package org.lotka.xenonx.presentation.ui.screens.chats.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.composables.etc.MobinButton
import org.lotka.xenonx.presentation.composables.etc.general.LottieLoading
import org.lotka.xenonx.presentation.composables.reusables.topbar.HomeTopBar
import org.lotka.xenonx.presentation.theme.AuthTheme
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.White
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.ui.navigation.BackPressHandler
import org.lotka.xenonx.presentation.ui.navigation.onBackPressedFunctionToFinishApp
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.ListingFilterBottomSheet
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.ListingItemBottomSheet
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.PlpBottomSheetType
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.SearchCityBottomSheet
import org.lotka.xenonx.presentation.util.UIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.lotka.xenonx.presentation.composables.HeaderText
import org.lotka.xenonx.presentation.composables.TextFieldHeader
import org.lotka.xenonx.presentation.composables.etc.KilidTextField
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.ui.screens.chats.home.profile.ProfileViewModel
import org.lotka.xenonx.presentation.ui.screens.plp.PlpScreenEvent
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
import org.lotka.xenonx.presentation.ui.screens.plp.compose.HomeTabRow
import timber.log.Timber
import java.util.Collections.emptyList

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class,

    )
@Composable
fun HomeChatScreen(
    navController: NavController,
    viewModel: PlpViewModel,
    profileViewModel: ProfileViewModel,
    onNavigateToRecipeDetailScreen: (String) -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {

    var canPop by remember { mutableStateOf(false) }
    val page = viewModel.page.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val dialogQueue = viewModel.dialogQueue
    val loading = viewModel.isActiveJobRunning.value
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val sessions by viewModel.sessions.collectAsState(initial = emptyList())
    val uiState by viewModel.sessionUiState.collectAsState(initial = UIState.Idle)

    val lazyListState = rememberLazyListState()
    val savedScrollIndex = viewModel.savedScrollIndex


    LaunchedEffect(key1 = savedScrollIndex) {
        Timber.tag("pdptest").d("in first launchd effect: ")
        lazyListState.scrollToItem(savedScrollIndex)
    }

    // Create the ModalBottomSheetState with dynamic skipHalfExpanded value
    val halfScreenBottomSheet = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )

    val fullScreenBottomSheet = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    LaunchedEffect(fullScreenBottomSheet) {
        snapshotFlow { fullScreenBottomSheet.currentValue }
            .collect { state ->
                if (state == ModalBottomSheetValue.Hidden) {
                    viewModel.onFilterBottomSheetClosed()
                    keyboardController?.hide()
                }
            }
    }

    LaunchedEffect(halfScreenBottomSheet) {
        snapshotFlow { halfScreenBottomSheet.currentValue }
            .collect { state ->
                if (state == ModalBottomSheetValue.Hidden) {
                    viewModel.onFilterBottomSheetClosed()
                }
            }
    }



    navController.addOnDestinationChangedListener { controller, _, _ ->
        viewModel.showBottomSheet(PlpBottomSheetType.NONE)
        canPop = controller.previousBackStackEntry != null
    }

    val onBack: () -> Unit = {
        coroutineScope.launch {
            if (fullScreenBottomSheet.currentValue != ModalBottomSheetValue.Hidden) {
                viewModel.showBottomSheet(PlpBottomSheetType.NONE)
                fullScreenBottomSheet.hide()
                return@launch
            } else if (halfScreenBottomSheet.currentValue != ModalBottomSheetValue.Hidden) {
                viewModel.showBottomSheet(PlpBottomSheetType.NONE)
                halfScreenBottomSheet.hide()
            } else {
                onBackPressedFunctionToFinishApp(context)
            }
        }
    }
    BackPressHandler(onBackPressed = onBack)

    ModalBottomSheetLayout(
        sheetState = fullScreenBottomSheet,
        sheetBackgroundColor = Color.Transparent,
        scrimColor = colorResource(id = R.color.transparent_grey),
        sheetElevation = 30.dp,
        sheetContent = {
            when (viewModel.fullScreenActiveBottomSheet) {

                PlpBottomSheetType.LOCATION_SEARCH -> {
                    SearchCityBottomSheet(
                        viewModel = viewModel,
                        modalBottomSheetState = fullScreenBottomSheet,
                        coroutineScope = coroutineScope,
                        onApplyFilterClicked = {
//                            coroutineScope.launch {
//                                viewModel.showBottomSheet(PlpBottomSheetType.NONE)
//                                halfScreenBottomSheet.hide()
//                            }
                        },
                        onDismissFilterClicked = {
                            coroutineScope.launch {
                                // viewModel.showBottomSheet(PlpBottomSheetType.NONE)
                                fullScreenBottomSheet.hide()
                            }
                        },
                        configuration = configuration,
                        isDarkTheme = isDarkTheme,
                        selectedAreas = viewModel.filterManager.getSelectedLocations(),
                        onAddNewArea = {
                            viewModel.toggleCitySelection(it)
                        },
                        onRemoveArea = {
                            viewModel.toggleCitySelection(it)
                        }
                    )
                }

                else -> {

                }
            }
        },
    )
    {

        ModalBottomSheetLayout(
            sheetState = halfScreenBottomSheet,
            sheetBackgroundColor = Color.Transparent,
            scrimColor = colorResource(id = R.color.transparent_grey),
            sheetElevation = 30.dp,
            sheetContent = {
                when (viewModel.halfScreenActiveBottomSheet) {
                    PlpBottomSheetType.LISTING_ITEM -> {
                        ListingItemBottomSheet(
                            viewModel, halfScreenBottomSheet, coroutineScope
                        )
                    }

                    PlpBottomSheetType.FILTER -> {
                        ListingFilterBottomSheet(
                            viewModel = viewModel,
                            halfScreenModalBottomSheet = halfScreenBottomSheet,
                            fullScreenModalBottomSheet = fullScreenBottomSheet,
                            coroutineScope = coroutineScope,
                            onApplyFilterClicked = {
                                coroutineScope.launch {
                                    viewModel.showBottomSheet(PlpBottomSheetType.NONE)
                                    halfScreenBottomSheet.hide()
                                }
                            },
                            onDismissFilterClicked = {
                                coroutineScope.launch {
                                    viewModel.showBottomSheet(PlpBottomSheetType.NONE)
                                    halfScreenBottomSheet.hide()
                                }
                            },
                            configuration = configuration,
                            isDarkTheme = isDarkTheme
                        )

                    }

                    else -> {

                    }
                }
            },
        ) {


            AuthTheme(
                darkTheme = isDarkTheme,
                isNetworkAvailable = true,
                displayProgressBar = loading,
                scaffoldState = scaffoldState,
                dialogQueue = dialogQueue.queue.value

            ) {
                val chats = viewModel.chats
                val userData = profileViewModel.userData
                val showDialog = remember {
                    mutableStateOf(false)
                }
                val onFabClick:()->Unit = {  showDialog.value = true}
                val onDismiss:()->Unit = {  showDialog.value = false}
                val onAddChat:()->Unit = {
//                viewModel.onAddChat(it)
                    showDialog.value = false
                }





                Scaffold(

                    modifier = Modifier.fillMaxSize(),

                    bottomBar = {
                        BottomNavigationItemMenu(
                            selectItem = BottomNavigationItem.CHATLIST,
                            navController = navController
                        )
                    },
                    floatingActionButton = {
                        fabNavigation(
                            onFabClick = { onFabClick() },
                            onDismiss = { onDismiss() },
                            showDialog = showDialog.value,
                            onAddChat = {
                            onAddChat()
                            }
                        )
                    },
                    topBar = {
                        HomeTopBar(
                            onClick = {},
                            mainScreens = true,
                            onToggleTheme = {
                                onToggleTheme()
                            },
                            onBackPressed = onBack,
                            isDarkMode = isDarkTheme,
                        )
                    }, drawerElevation = 0.dp,
                    drawerGesturesEnabled = false,
                    drawerContentColor = Color.White,
                    drawerScrimColor = Color.White,


                    content = {

                        if (chats.value.isEmpty()) {
                            Column (modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                TextFieldHeader(text = "No Chats Found")
                            }
                        }





                        val swipeRefreshState = rememberPullRefreshState(
                            refreshing = uiState == UIState.Loading,
                            onRefresh = {
                                viewModel.onTriggerEvent(PlpScreenEvent.NewSearchEvent)
                            }
                        )

                        Column(modifier = Modifier.background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)) {


                            val context = LocalContext.current

                            AnimatedVisibility(
                                visible = viewModel.isIndicatedUpdateAvailable,
                                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                                        expandVertically(animationSpec = tween(durationMillis = 1000)),
                                exit = fadeOut(animationSpec = tween(durationMillis = 1000)) +
                                        shrinkVertically(animationSpec = tween(durationMillis = 1000))
                            ) {
                                Column(modifier = Modifier

                                    .fillMaxWidth()
                                    .background(kilidPrimaryColor)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data =
                                                Uri.parse("market://details?id=${context.packageName}")
                                            // Fallback URL in case the Play Store app is not installed
                                            val fallbackUrl =
                                                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                                            val chooser = Intent.createChooser(this, "Open with")
                                            // Add the fallback URL to the Intent
                                            this.putExtra(
                                                Intent.EXTRA_INITIAL_INTENTS,
                                                arrayOf(Intent(Intent.ACTION_VIEW, fallbackUrl))
                                            )
                                        }
                                        context.startActivity(intent)
                                    }) {
                                    Divider(color = White.copy(alpha = 0.3f))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(kilidPrimaryColor)
                                            .padding(vertical = 8.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "آپدیت جدید در دسترس است",
                                            style = KilidTypography.h3,
                                            color = White
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = "نصب",
                                            style = KilidTypography.h3,
                                            color = White
                                        )
                                    }
                                }

                            }
//                            PlpFilterPart(
//                                isDarkTheme = isDarkTheme,
//                                viewModel = viewModel,
//                                configuration = configuration,
//                                coroutineScope = coroutineScope,
//                                onFilterClick = {
//                                    keyboardController?.hide()
//                                    coroutineScope.launch {
//                                        viewModel.showBottomSheet(PlpBottomSheetType.FILTER)
//                                        viewModel.onHalfScreenBottomSheetOpened()
//                                        halfScreenBottomSheet.show()
//
//                                    }
//                                },
//                                onLocationSelectClick = {
//                                    keyboardController?.hide()
//                                    coroutineScope.launch {
//                                        viewModel.showBottomSheet(PlpBottomSheetType.LOCATION_SEARCH)
//                                        fullScreenBottomSheet.show()
//                                    }
//                                },
//                            )

                            Divider()

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pullRefresh(swipeRefreshState)
                            )
                            {

                                HomeTabRow(
                                    navController = navController,
                                    viewModel = viewModel,
                                    onNavigateToRecipeDetailScreen = onNavigateToRecipeDetailScreen,
                                    onToggleTheme = onToggleTheme,
                                    isDarkTheme = isDarkTheme
                                )



                                this@Column.AnimatedVisibility(
                                    visible = uiState == UIState.PaginationLoading,
                                    enter = slideInVertically(
                                        // Start the slide from the bottom of the screen
                                        initialOffsetY = { fullHeight -> fullHeight },
                                        animationSpec = tween(durationMillis = 700)
                                    ),
                                    exit = slideOutVertically(
                                        // Exit towards the bottom of the screen
                                        targetOffsetY = { fullHeight -> fullHeight },
                                        animationSpec = tween(durationMillis = 700)
                                    ), modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Transparent)
                                        .align(Alignment.BottomCenter)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White)
                                            .align(Alignment.BottomCenter)
                                    ) {
                                        Divider()
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(
                                                text = "در حال دریافت موارد بیشتر ...",
                                                style = KilidTypography.h3
                                            )
                                            LottieLoading(size = 40.dp)
                                        }
                                    }
                                }

                                this@Column.AnimatedVisibility(visible = uiState == UIState.PaginationError,
                                    enter = slideInVertically(
                                        // Start the slide from the bottom of the screen
                                        initialOffsetY = { fullHeight -> fullHeight },
                                        animationSpec = tween(durationMillis = 700)
                                    ),
                                    exit = slideOutVertically(
                                        // Exit towards the bottom of the screen
                                        targetOffsetY = { fullHeight -> fullHeight },
                                        animationSpec = tween(durationMillis = 700)
                                    ), modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Transparent)
                                        .align(Alignment.BottomCenter)
                                        .clickable {
                                            viewModel.sessionUiState.value = UIState.Idle
                                            viewModel.onTriggerEvent(PlpScreenEvent.NextPageEvent)
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(kilidPrimaryColor)
                                            .align(Alignment.BottomCenter),
                                        horizontalAlignment = CenterHorizontally,
                                        verticalArrangement = Center
                                    ) {
                                        Spacer(modifier = Modifier.size(20.dp))
                                        Column(modifier = Modifier.size(150.dp)) {
                                            val composition by rememberLottieComposition(
                                                LottieCompositionSpec.RawRes(
                                                    R.raw.kilid_no_netwok
                                                )
                                            )
                                            val progress by animateLottieCompositionAsState(
                                                composition,
                                                iterations = 50,
                                                reverseOnRepeat = false
                                            )
                                            LottieAnimation(
                                                composition = composition,
                                                progress = { progress },
                                                modifier = Modifier
                                                    .size(150.dp)
                                            )
                                        }

                                        Text(
                                            text = "ارتباط بااینترنت برقرار نشد",
                                            style = KilidTypography.h3,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        MobinButton(
                                            title = "تلاش مجدد",
                                            onClick = {
                                                viewModel.sessionUiState.value = UIState.Idle
                                                viewModel.onTriggerEvent(PlpScreenEvent.NextPageEvent)
                                            },
                                            outline = true,
                                            modifier = Modifier
                                                .height(40.dp)
                                                .border(
                                                    BorderStroke(
                                                        width = 2.dp,
                                                        color = kilidPrimaryColor
                                                    ), RoundedCornerShape(8.dp)
                                                )
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))

                                    }
                                }

                            }
                        }


                    })


            }


        }


    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun fabNavigation(
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    showDialog: Boolean,
    onAddChat: (String) -> Unit
) {

    val addChatMember = remember {
        mutableStateOf("")
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss.invoke() },
            buttons = {
                Button(onClick = { onAddChat.invoke(addChatMember.value) }
                ) {
                    TextFieldHeader(text = "Add Chat")
                }
            },
            title = {
                HeaderText(text = "Add Chat")
            },
            text = {
                KilidTextField(
                    onTextChanged = {
                        addChatMember.value = it
                    },
                    value = addChatMember.value,
                    label = R.string.add_chat,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            }

        )
        FloatingActionButton(onClick = { onFabClick.invoke() },
            backgroundColor = kilidPrimaryColor,
            shape = CircleShape,
            modifier = Modifier.padding(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = null,
                tint = Color.White
            )
        }
    }

}







