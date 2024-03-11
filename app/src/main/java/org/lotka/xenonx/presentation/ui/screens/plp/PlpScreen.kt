package org.lotka.xenonx.presentation.ui.screens.plp

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.presentation.ui.navigation.BackPressHandler
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.ui.navigation.onBackPressedFunctionToFinishApp
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.ListingFilterBottomSheet
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.ListingItemBottomSheet
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.PlpBottomSheetType
import org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet.SearchCityBottomSheet
import org.lotka.xenonx.presentation.ui.screens.plp.compose.PlpItem
import org.lotka.xenonx.presentation.ui.screens.plp.compose.PlpFilterPart
import org.lotka.xenonx.presentation.util.UIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.lotka.xenonx.presentation.theme.TelegramBackGround
import org.lotka.xenonx.presentation.ui.screens.plp.compose.HomeTabRow
import timber.log.Timber
import java.util.Collections
import java.util.Collections.emptyList

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun PlpScreen(
    navController: NavController,
    viewModel: PlpViewModel,
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
        skipHalfExpanded =  false
    )

    val fullScreenBottomSheet = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded =  true
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
                        isDarkTheme= isDarkTheme,
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

                Scaffold(
                    modifier = Modifier.fillMaxSize().
                    background(color = TelegramBackGround),
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
                },  drawerElevation = 0.dp,
                    drawerGesturesEnabled = false,
                    drawerContentColor = Color.White,
                    drawerScrimColor = Color.White,
                    

                    content = {
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
                                    .background(TelegramBackGround)
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
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .background(TelegramBackGround)
                                        .padding(vertical = 8.dp, horizontal = 12.dp)   ,
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center) {
                                        Text(text = "آپدیت جدید در دسترس است" , style = KilidTypography.h3 , color = White)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(text = "نصب" , style = KilidTypography.h3 , color = White)
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

//                                val page = viewModel.page.value
//                                val loading = viewModel.isActiveJobRunning.value
//
//                                val configuration = LocalConfiguration.current
//
//                                val sessions by viewModel.sessions.collectAsState(initial = Collections.emptyList())
//                                val uiState by viewModel.sessionUiState.collectAsState(initial = UIState.Idle)
//
//                                val lazyListState = rememberLazyListState()



                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (isDarkTheme) Color.White else Color.White),
                                    state = lazyListState,
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {


                                    when (uiState) {
                                        is UIState.Error -> {
                                            item {
                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.height(
                                                        (configuration.screenHeightDp.dp / 3) * 2
                                                    )

                                                ) {
                                                    Text(
                                                        text = "خطا در ارتباط با شبکه",
                                                        style = KilidTypography.h4.copy(fontSize = 18.sp),
                                                        color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts
                                                    )
                                                    Text(
                                                        text = "لطفا اتصال به اینترنت را بررسی کنید ",
                                                        style = KilidTypography.h3.copy(fontSize = 14.sp),
                                                        color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts
                                                    )
                                                    Spacer(modifier = Modifier.height(16.dp))


                                                    MobinButton(
                                                        title = "تلاش مجدد",
                                                        onClick = {
                                                            viewModel.onTriggerEvent(
                                                                PlpScreenEvent.NewSearchEvent
                                                            )
                                                        },
                                                        outline = true,
                                                        modifier = Modifier
                                                            .height(40.dp)
                                                            .padding(horizontal = 32.dp)
                                                            .border(
                                                                BorderStroke(
                                                                    width = 2.dp,
                                                                    color = kilidPrimaryColor
                                                                ), RoundedCornerShape(8.dp)
                                                            )
                                                    )
                                                }
                                            }
                                        }

                                        UIState.Loading -> {
                                            item {
                                                Box(
                                                    modifier = Modifier
                                                        .height((configuration.screenHeightDp.dp / 3) * 2)
                                                        .fillMaxWidth(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    LottieLoading(size = 150.dp)
                                                }
                                            }
                                        }

                                        else -> {

                                            if (sessions.isEmpty() && uiState == UIState.Success) {
                                                item {
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .fillMaxWidth()
                                                            .height(
                                                                100.dp
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {

                                                        if (viewModel.filterManager.getActiveFilters()
                                                                .isEmpty()
                                                        ) {

                                                            Text(
                                                                text = "شما هیچ آگهی ندارید \n از طریق دکمه زیر آگهی ثبت کنید",
                                                                style = KilidTypography.h3,
                                                                textAlign = TextAlign.Center
                                                            )

                                                        } else {

                                                            Column(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                verticalArrangement = Arrangement.Center,
                                                                horizontalAlignment = Alignment.CenterHorizontally
                                                            ) {

                                                                Text(
                                                                    text = "برای این دسته بندی آگهی وجود ندارد",
                                                                    style = KilidTypography.h3
                                                                )

                                                                Text(
                                                                    text = "حذف همه فیلتر ها",
                                                                    style = KilidTypography.h3.copy(
                                                                        color = Color.Blue
                                                                    ),
                                                                    modifier = Modifier.clickable {
                                                                        viewModel.clearAllFilters()
                                                                    }
                                                                )
                                                            }

                                                        }
                                                    }
                                                }
                                            } else {

                                                itemsIndexed(items = sessions) { index, recipe ->
                                                    viewModel.latestIndex.value = index
                                                    if (uiState != UIState.PaginationError) {
                                                        viewModel.onChangeRecipeScrollPosition(index)

                                                        if (((index + 1) >= PAGE_SIZE) && (page == 0) && !loading) {
                                                            Timber.tag("pagination")
                                                                .d("for zero page --- latest index : " + index + " and ")
                                                            viewModel.onTriggerEvent(PlpScreenEvent.NextPageEvent)
                                                        } else if ((index + 3) >= (((page + 1) * PAGE_SIZE)) && !loading) {
//                                                            Timber.tag("pagination").d("for ${viewModel.calculateOutput(page)} other page --- latest index : " + index + " and page is: ${page} and multiple ${(page * PAGE_SIZE)} ")
                                                            viewModel.onTriggerEvent(PlpScreenEvent.NextPageEvent)
                                                        }

                                                    }




                                                    if (recipe != null) {

                                                        PlpItem(
                                                            isDarkTheme = isDarkTheme,
                                                            item = recipe,
                                                            screen = configuration,
                                                            onMoreClicked = {
//                                                                coroutineScope.launch {
//                                                                    viewModel.showBottomSheet(
//                                                                        PlpBottomSheetType.LISTING_ITEM
//                                                                    )
//                                                                    halfScreenBottomSheet.show()
//                                                                }
                                                            },
                                                            onClicked = {
                                                                viewModel.onHalfScreenBottomSheetOpened()
                                                                viewModel.savedScrollIndex = index
                                                                val route =
                                                                    HomeScreensNavigation.pdp.route + "/${it}"
                                                                onNavigateToRecipeDetailScreen(route)
                                                            },
                                                            onLadderUpClick = {},
                                                            onFeaturedClick = {},
                                                            index = index
                                                        )


                                                    }
                                                }
                                            }


                                        }
                                    }



                            }


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
//                                        .align(Alignment.BottomCenter)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White)
//                                            .align(Alignment.BottomCenter)
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
//                                        .align(Alignment.BottomCenter)
                                        .clickable {
                                            viewModel.sessionUiState.value = UIState.Idle
                                            viewModel.onTriggerEvent(PlpScreenEvent.NextPageEvent)
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(kilidPrimaryColor)
//                                            .align(Alignment.BottomCenter),
                                       , horizontalAlignment = CenterHorizontally,
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














