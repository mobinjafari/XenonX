package org.lotka.xenonx.presentation.ui.screens.pdp

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import org.lotka.xenonx.R
import org.lotka.xenonx.domain.enums.ListingType
import org.lotka.xenonx.presentation.composables.Color.getObjectAvailabilityColorFilter
import org.lotka.xenonx.presentation.composables.Color.getTextAvailabilityColorFilter
import org.lotka.xenonx.presentation.composables.etc.AutoSlidingCarousel
import org.lotka.xenonx.presentation.composables.etc.MobinButton
import org.lotka.xenonx.presentation.composables.reusables.topbar.HomeTopBar
import org.lotka.xenonx.presentation.theme.AuthTheme
import org.lotka.xenonx.presentation.theme.White
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidDarkObjects
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteObjects
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor
import org.lotka.xenonx.presentation.theme.newThirdTextColor
import org.lotka.xenonx.presentation.ui.navigation.BackPressHandler
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.ui.screens.pdp.bottom_sheet.ContactAgentBottomSheet
import org.lotka.xenonx.presentation.ui.screens.pdp.bottom_sheet.PdpBottomSheetType
import org.lotka.xenonx.presentation.util.UIState
import org.lotka.xenonx.util.extension.formatAsCurrency
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PdpScreen(
    navController: NavController,
    viewModel: PdpViewModel = hiltViewModel(),
    recipeId: Int?,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    // Use LaunchedEffect to run code once per recipeId

    val identifier by remember {
        mutableStateOf(recipeId)
    }

    val pdpModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val pdpDetail by viewModel.pdpDetail.collectAsState(initial = null)
    val pdpDetailUiState by viewModel.pdpDetailUiState.collectAsState(initial = UIState.Loading)
    val contactInformation by viewModel.contactInformation.collectAsState(initial = null)
    val contactInformationUIState by viewModel.contactInformationUiState.collectAsState(initial = UIState.Loading)

    if (recipeId != null) {
        if (!viewModel.onLoad.value) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(PdpScreenEvent.GetRecipeEvent(recipeId))
        }

        LaunchedEffect(recipeId) {
            recipeId.let {
                if(recipeId != 0){
                    viewModel.onTriggerEvent(PdpScreenEvent.SendTrackerEvent(recipeId))
                }
            }
        }

    }


    val screen = LocalConfiguration.current
    val context = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val dialogQueue = viewModel.dialogQueue
    val loading = viewModel.isActiveJobRunning.value


    val onBack: () -> Unit = {
        if (viewModel.selectedImageIndex != -1) {
            viewModel.selectedImageIndex = -1
        } else {
            coroutineScope.launch {
                navController.navigate(HomeScreensNavigation.plp.route)
            }
        }
    }
    BackPressHandler(onBackPressed = onBack)



    ModalBottomSheetLayout(
        sheetState = pdpModalBottomSheetState,
        sheetBackgroundColor = Color.Transparent,
        scrimColor = colorResource(id = R.color.transparent_grey),
        sheetElevation = 30.dp,
        sheetContent = {
            when (viewModel.activeBottomSheet) {
                PdpBottomSheetType.CONTACT_AGENT -> {
                    identifier?.let { it ->
                        ContactAgentBottomSheet(
                            contactInformation = contactInformation ,
                            contactInformationUIState = contactInformationUIState,
                            modalBottomSheetState =  pdpModalBottomSheetState,
                            coroutineScope =coroutineScope,
                            identifier = it,
                        )
                    } ?: run{
                        //when is null
                    }
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
                topBar = {
                    HomeTopBar(
                        mainScreens = false,
                        onClick = {},
                        onBackPressed = { onBack() },
                        onToggleTheme = { onToggleTheme() },
                        isDarkMode = isDarkTheme
                    )
                },
                content = {


                    val swipeRefreshState = rememberPullRefreshState(
                        refreshing = pdpDetailUiState == UIState.Loading,
                        onRefresh = {
                            if (identifier != null) {
                                viewModel.onTriggerEvent(
                                    PdpScreenEvent.GetRecipeEvent(
                                        identifier ?: 0
                                    )
                                )
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .pullRefresh(swipeRefreshState)
                            .fillMaxSize()
                            .alpha(if (pdpDetailUiState == UIState.Loading) 0.5f else 1f)
                            .background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound),
                    ) {


                        if (pdpDetailUiState == UIState.Loading) {
                            // Show loading indicator
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(
                                    R.raw.loading
                                )
                            )
                            val progress by animateLottieCompositionAsState(
                                composition, iterations = 50, reverseOnRepeat = false
                            )
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier
                                    .size(150.dp)
                                    .align(Center)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.Start
                            ) {



                                item {
                                    Spacer(modifier = Modifier.size(12.dp))
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = pdpDetail?.title ?: "",
                                            color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                            style = MaterialTheme.typography.h5.copy(
                                                fontSize = 16.sp
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                val hasImages: Boolean =
                                    pdpDetail?.largePictures?.isEmpty() == false
                                val images: List<String> = pdpDetail?.largePictures ?: listOf()
                                if (hasImages) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)

                                                .fillMaxWidth()
                                                .height(((screen.screenHeightDp).dp / 10) * 3),
                                            contentAlignment = Center
                                        ) {



                                            Card(
                                                shape = RectangleShape,
                                                modifier = Modifier
                                                    .background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)
                                                    .fillMaxWidth()
                                                    .height(((screen.screenHeightDp).dp / 10) * 3)
                                            ) {
                                                AutoSlidingCarousel(
                                                    itemsCount = pdpDetail?.largePictures?.size
                                                        ?: 0,
                                                    itemContent = { index ->
                                                        var retryHash by remember {
                                                            mutableIntStateOf(
                                                                value = 0
                                                            )
                                                        }
                                                        SubcomposeAsyncImage(
                                                            model = ImageRequest.Builder(
                                                                LocalContext.current
                                                            ).data(data = images[index])
                                                                .apply(block = fun ImageRequest.Builder.() {
                                                                    crossfade(false)
                                                                    setParameter(
                                                                        "retry_hash",
                                                                        retryHash,
                                                                        memoryCacheKey = null
                                                                    )
                                                                    memoryCachePolicy(
                                                                        CachePolicy.ENABLED
                                                                    )
                                                                    diskCachePolicy(CachePolicy.ENABLED)
                                                                    networkCachePolicy(
                                                                        CachePolicy.ENABLED
                                                                    )
                                                                })
                                                                .build(),
                                                            contentDescription = stringResource(
                                                                R.string.description
                                                            ),
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)
                                                                .fillMaxWidth()
                                                                .height(((screen.screenHeightDp).dp / 10) * 3)


                                                        ) {
                                                            when (val state = painter.state) {
                                                                is AsyncImagePainter.State.Loading -> {
                                                                    Box(modifier = Modifier.size(100.dp)) {


                                                                        val composition1 by rememberLottieComposition(
                                                                            LottieCompositionSpec.RawRes(
                                                                                R.raw.loading
                                                                            )
                                                                        )
                                                                        val progress1 by animateLottieCompositionAsState(
                                                                            composition1,
                                                                            iterations = 50,
                                                                            reverseOnRepeat = false
                                                                        )
                                                                        LottieAnimation(
                                                                            composition = composition1,
                                                                            progress = { progress1 },
                                                                            modifier = Modifier
                                                                                .height(140.dp)
                                                                                .width(140.dp)
                                                                                .align(Center)
                                                                        )


                                                                    }

                                                                }

                                                                is AsyncImagePainter.State.Error -> {
                                                                    Column(
                                                                        Modifier
                                                                            .background(
                                                                                White
                                                                            )
                                                                            .align(Center)
                                                                    ) {
                                                                        val composition2 by rememberLottieComposition(
                                                                            LottieCompositionSpec.RawRes(
                                                                                R.raw.image_processing
                                                                            )
                                                                        )
                                                                        val progress2 by animateLottieCompositionAsState(
                                                                            composition2,
                                                                            iterations = 50,
                                                                            reverseOnRepeat = false
                                                                        )
                                                                        LottieAnimation(
                                                                            composition = composition2,
                                                                            progress = { progress2 },
                                                                            modifier = Modifier
                                                                                .fillMaxWidth()
                                                                                .size(80.dp)
                                                                        )
                                                                        Text(
                                                                            text = "خطایی رخ داده است",
                                                                            style = MaterialTheme.typography.h4.copy(
                                                                                fontSize = 12.sp
                                                                            ),
                                                                            modifier = Modifier.align(
                                                                                CenterHorizontally
                                                                            )
                                                                        )
                                                                        Text(
                                                                            text = "در حال تلاش مجدد هستیم ",
                                                                            style = MaterialTheme.typography.h4.copy(
                                                                                fontSize = 12.sp
                                                                            ),
                                                                            modifier = Modifier.align(
                                                                                CenterHorizontally
                                                                            )
                                                                        )
                                                                    }
                                                                    //retry after 2 seconds
                                                                    LaunchedEffect(state) {
                                                                        delay(2000)
                                                                        retryHash++
                                                                    }


                                                                }

                                                                else -> {
                                                                    SubcomposeAsyncImageContent()
                                                                }
                                                            }
                                                        }


                                                    },
                                                    onImageClicked = {
                                                        viewModel.selectedImageIndex = it
                                                    })
                                            }

                                            Row (horizontalArrangement = Arrangement.Center ,
                                                verticalAlignment = Alignment.CenterVertically ,
                                                modifier = Modifier
                                                    .padding(4.dp)
                                                    .background(
                                                color = Color.White.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(0.dp)
                                            ).align(Alignment.TopEnd).padding(4.dp),

                                            ) {
                                                pdpDetail?.agencyLogo?.let{
                                                    Image(
                                                        painter = rememberAsyncImagePainter(model =it),
                                                        contentDescription = "",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .width(30.dp)
                                                            .height(30.dp)
                                                            .clip(CircleShape) // This clips the image to a circle shape
                                                            .border(
                                                                1.dp,
                                                                kilidPrimaryColor,
                                                                CircleShape
                                                            ) // This adds a border; adjust thickness (2.dp) and color (Color.Black) as needed
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = pdpDetail?.agentName ?: "",
                                                    color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                    style = MaterialTheme.typography.h3.copy(
                                                        fontSize = 13.sp
                                                    ),
                                                    modifier = Modifier
                                                        .align(CenterVertically)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .background(Color.White)
                                                .width((screen.screenWidthDp).dp)
                                                .height(((screen.screenHeightDp).dp / 10) * 3),
                                            contentAlignment = Center
                                        ) {


                                            Image(
                                                painter = painterResource(id = R.drawable.nd_noimage),
                                                contentDescription = "ni image",
                                                modifier = Modifier
                                                    .width((screen.screenWidthDp).dp)
                                                    .height(((screen.screenHeightDp).dp / 10) * 3)
                                            )

                                            Text(
                                                text = stringResource(id = R.string.image_description_short),
                                                style = MaterialTheme.typography.h2.copy(
                                                    fontSize = 13.sp
                                                ),
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                modifier = Modifier
                                                    .padding(16.dp)
                                                    .background(
                                                        Color.LightGray,
                                                        RoundedCornerShape(25.dp)
                                                    )
                                                    .padding(2.dp)
                                                    .align(BottomStart)
                                            )
                                        }


                                    }
                                }



                                /*
                                Prices
                                 */

                                item {
                                    Spacer(modifier = Modifier.height(18.dp))
                                    if (pdpDetail?.listingType==ListingType.SALE_AND_PRE_SALE) {

                                        Column(
                                            modifier = Modifier.padding(horizontal = 12.dp),
                                        ) {

                                            val totalPriceText =
                                                when {
                                                    pdpDetail?.priceOrMortgage == 0L -> "رایگان"
                                                    pdpDetail?.agreedPrice == true  -> "توافقی"
                                                    pdpDetail?.priceOrMortgage!! > 0 -> pdpDetail?.priceOrMortgage?.formatAsCurrency()
                                                    else -> "-"
                                                }

                                            Text(
                                                text = stringResource(id = R.string.total_price) + " : " +  totalPriceText,
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                style = MaterialTheme.typography.h5.copy(
                                                    fontSize = 18.sp, color = newThirdTextColor
                                                ),
                                            )




                                        val pricePerMeter =
                                            stringResource(id = R.string.square_meters_price2) + " : " + when {
                                                pdpDetail?.unitPriceOrRent == -1L -> "تعیین نشده"
                                                pdpDetail?.unitPriceOrRent == 0L -> "رایگان"
                                                pdpDetail?.agreedPrice == true -> "توافقی"
                                                pdpDetail?.unitPriceOrRent!! > 0 -> pdpDetail?.unitPriceOrRent?.formatAsCurrency()
                                                else -> "-"
                                            }

                                        Text(
                                            text = pricePerMeter,
                                            color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                            style = MaterialTheme.typography.h4.copy(
                                                fontSize = 18.sp,
                                            ),

                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                        }

                                    }
                                    else if (pdpDetail?.listingType==ListingType.MORTGAGE_AND_RENT) {
                                        Column(
                                            modifier = Modifier.padding(horizontal = 12.dp),
                                        ) {

                                            val mortgageText = when {
                                                pdpDetail?.agreedPrice == true -> "توافقی"
                                                pdpDetail?.priceOrMortgage == 0L -> "رایگان"
                                                pdpDetail?.priceOrMortgage?.let { it >= 0L } == true -> pdpDetail?.priceOrMortgage?.formatAsCurrency()
                                                else -> "-"
                                            }


                                            Text(
                                                text = stringResource(id = R.string.mortgage) + " : " + mortgageText,
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                style = MaterialTheme.typography.h5.copy(
                                                    fontSize = 20.sp
                                                ),
                                            )

                                            val rentText =
                                                when {
                                                    pdpDetail?.fullyMortgage == true -> "رایگان (رهن کامل)"
                                                    pdpDetail?.agreedPrice == true -> "توافقی"
                                                    pdpDetail?.unitPriceOrRent?.let { it >= 0L } == true-> pdpDetail?.unitPriceOrRent?.formatAsCurrency()

                                                    else -> ""
                                                }

                                            Text(
                                                text =    stringResource(id = R.string.rent) + " : " +  rentText,
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                style = MaterialTheme.typography.h4.copy(
                                                    fontSize = 18.sp
                                                ),

                                                )
                                        }
                                    }


                                }

                                /*
                                                               Pills
                                                                */
                                //     parking , elevator  ,  storage

                                item {




                                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                            Spacer(modifier = Modifier.width(8.dp))

                                            if (pdpDetail?.floorArea != null) {
                                                Text(
                                                    text = pdpDetail?.floorArea.toString() + "  متر",
                                                    style = MaterialTheme.typography.h4.copy(
                                                        fontSize = 15.sp
                                                    ),
                                                    color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                    modifier = Modifier
                                                        .background(
                                                            if (isDarkTheme) kilidDarkObjects else kilidWhiteObjects,
                                                            RoundedCornerShape(25.dp)
                                                        )
                                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                                        .align(CenterVertically)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                            }



                                            if (pdpDetail?.age != null) {

                                                val ageText: String =
                                                    if (pdpDetail?.age == 0) {
                                                        "نوساز"
                                                    } else {
                                                        pdpDetail?.age.toString() + "  ساله"
                                                    }

                                                Text(
                                                    text = ageText,
                                                    style = MaterialTheme.typography.h4.copy(
                                                        fontSize = 15.sp
                                                    ),
                                                    color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                    modifier = Modifier
                                                        .background(
                                                            if (isDarkTheme) kilidDarkObjects else kilidWhiteObjects,
                                                            RoundedCornerShape(25.dp)
                                                        )
                                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                                        .align(CenterVertically)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                            }

                                            // Number of Beds

                                            val numberOfBedsText: String =
                                                when (pdpDetail?.numberOfRooms) {
                                                    0 -> {
                                                        "بدون خواب"
                                                    }
                                                    null -> {
                                                        "بدون خواب"
                                                    }
                                                    else -> {
                                                        pdpDetail?.numberOfRooms.toString() + "  خوابه"
                                                    }
                                                }

                                            Text(
                                                text = numberOfBedsText,
                                                style = MaterialTheme.typography.h4.copy(
                                                    fontSize = 15.sp
                                                ),
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                modifier = Modifier
                                                    .background(
                                                        if (isDarkTheme) kilidDarkObjects else kilidWhiteObjects,
                                                        RoundedCornerShape(25.dp)
                                                    )
                                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                                    .align(CenterVertically)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))





                                            Text(
                                                text = pdpDetail?.landUseTypes?.nameFa?:"تجاری",
                                                style = MaterialTheme.typography.h4.copy(
                                                    fontSize = 15.sp
                                                ),
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                modifier = Modifier
                                                    .background(
                                                        if (isDarkTheme) kilidDarkObjects else kilidWhiteObjects,
                                                        RoundedCornerShape(25.dp)
                                                    )
                                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                                    .align(CenterVertically)
                                            )

                                            Spacer(modifier = Modifier.height(20.dp))
                                        }
                                    Divider(Modifier.padding(vertical = 16.dp))
                                    val isParkingAvailable = (pdpDetail?.numberOfParking?.let { it>0 }==true)
                                    val isStorageAvailable = (pdpDetail?.isStorageAvailable==true)
                                    val isElevatorAvailable = (pdpDetail?.isElevatorAvailable==true)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {

                                        Column(horizontalAlignment = CenterHorizontally) {
                                            Image(
                                                painter = painterResource(R.drawable.directions_car),
                                                contentDescription = "پارکینگ",
                                                modifier = Modifier.size(30.dp),
                                                colorFilter = getObjectAvailabilityColorFilter(available = isParkingAvailable , isDarkTheme = isDarkTheme )
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = if (isParkingAvailable) {
                                                    val numOfParking =
                                                        pdpDetail?.numberOfParking?.toInt() ?: 0
                                                    when (numOfParking) {
                                                        1 -> {
                                                            "پارکینگ  دارد"
                                                        }
                                                        else -> {
                                                            "${pdpDetail?.numberOfParking?.toInt()} پارکینگ دارد"
                                                        }
                                                    }

                                                } else {
                                                    "پارکینگ  ندارد"
                                                },
                                                style = MaterialTheme.typography.h3,
                                                color = getTextAvailabilityColorFilter(available = isParkingAvailable , isDarkTheme = isDarkTheme)
                                            )
                                        }

                                        //icon
                                        Column(horizontalAlignment = CenterHorizontally) {
                                            Image(
                                                painter = painterResource(R.drawable.home_storage),
                                                contentDescription = "انباری",
                                                modifier = Modifier.size(30.dp),
                                                colorFilter = getObjectAvailabilityColorFilter(available =isStorageAvailable , isDarkTheme = isDarkTheme )
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = if (isStorageAvailable) {
                                                    "انباری  دارد"
                                                } else {
                                                    "انباری  ندارد"
                                                },
                                                style = MaterialTheme.typography.h3,
                                                color = getTextAvailabilityColorFilter(available = isStorageAvailable , isDarkTheme = isDarkTheme)

                                            )
                                        }


                                        //icon
                                        Column(horizontalAlignment = CenterHorizontally) {
                                            Image(
                                                painter = painterResource(R.drawable.elevator),
                                                contentDescription = "آسانسور",
                                                modifier = Modifier.size(30.dp),
                                                colorFilter = getObjectAvailabilityColorFilter(available = isElevatorAvailable  , isDarkTheme = isDarkTheme)

                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = if (isElevatorAvailable) {
                                                    "آسانسور دارد"
                                                } else {
                                                    "آسانسور ندارد"
                                                },
                                                style = MaterialTheme.typography.h3,
                                                color = getTextAvailabilityColorFilter(available = isElevatorAvailable , isDarkTheme = isDarkTheme)
                                            )
                                        }
                                    }
                                }






                                /*
                                possibilities
                                 */



                                if (pdpDetail?.homeFeatures?.isNotEmpty() == true) {
                                    val featuresList: List<String> = pdpDetail?.homeFeatures?.let { lss->
                                        lss.mapNotNull { item -> item?.nameFa.toString() }
                                    }?: listOf()
                                    item {
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp)
                                        )
                                        Text(
                                            text = stringResource(id = R.string.possibilities),
                                            style = MaterialTheme.typography.h5.copy(
                                                fontSize = 22.sp
                                            ),
                                            color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                    items(featuresList.windowed(2, 2, partialWindows = true)) { pairOfFeatures ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 8.dp, end = 8.dp)
                                        ) {
                                            pairOfFeatures.forEach { feature ->
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(4.dp)
                                                ) {
                                                    Row(verticalAlignment = CenterVertically) {
                                                        Image(
                                                            painter = painterResource(R.drawable.nd_check),
                                                            contentDescription = "Edit Button",
                                                            modifier = Modifier.size(20.dp),
                                                            colorFilter = ColorFilter.tint(color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,)
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = feature,
                                                            color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                            style = MaterialTheme.typography.h3
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                                /*
                         conditions
                          */



//                                if (viewModel.conditions.value.isNotEmpty()) {
//                                    item {
//                                        Divider(
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .padding(vertical = 12.dp)
//                                        )
//                                        Text(
//                                            text = stringResource(id = R.string.conditions),
//                                            style = MaterialTheme.typography.h5.copy(
//                                                fontSize = 22.sp
//                                            ),
//                                            color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
//
//                                            modifier = Modifier.padding(horizontal = 16.dp)
//                                        )
//                                        Spacer(modifier = Modifier.height(10.dp))
//                                    }
//
//
//                                    val groupedConditions = viewModel.conditions.value.chunked(2)
//                                    items(groupedConditions) { pairOfCondition ->
//                                        Row(
//                                            modifier = Modifier
//                                                .fillMaxWidth()
//                                                .padding(start = 8.dp, end = 8.dp)
//                                        ) {
//                                            pairOfCondition.forEach { condition ->
//                                                Box(
//                                                    modifier = Modifier
//                                                        .weight(1f)
//                                                        .padding(4.dp)
//                                                ) {
//                                                    Row(verticalAlignment = CenterVertically) {
//                                                        Image(
//                                                            painter = painterResource(R.drawable.nd_check),
//                                                            contentDescription = "Edit Button",
//                                                            modifier = Modifier.size(20.dp)
//                                                        )
//                                                        Spacer(modifier = Modifier.width(6.dp))
//                                                        Text(
//                                                            text = condition,
//                                                            style = MaterialTheme.typography.h3
//                                                        )
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    item {
////                                        if (viewModel.loanVisibility || viewModel.unitShareVisibility) {
////                                            Divider(
////                                                modifier = Modifier
////                                                    .fillMaxWidth()
////                                                    .padding(16.dp)
////                                            )
////                                        }
//
////                                        if (viewModel.loanVisibility) {
////                                            Row(
////                                                verticalAlignment = CenterVertically,
////                                                horizontalArrangement = Arrangement.End,
////                                                modifier = Modifier.padding(horizontal = 16.dp)
////                                            ) {
////                                                Text(
////                                                    text = "مقدار وام : ${if (viewModel.mountOfLoan == "") "تعیین نشده" else viewModel.mountOfLoan.addThousandsSeparator() + " تومان"}  ",
////                                                    style = MaterialTheme.typography.h3
////                                                )
////                                            }
////                                        }
//
////                                        Spacer(modifier = Modifier.height(4.dp))
////
////
////                                        if (viewModel.unitShareVisibility) {
////                                            Row(
////                                                verticalAlignment = CenterVertically,
////                                                horizontalArrangement = Arrangement.End,
////                                                modifier = Modifier.padding(horizontal = 16.dp)
////                                            ) {
////                                                Text(
////                                                    text = "مقدار قدر السهم : ${if (viewModel.mountOfUnitShare == "") "تعیین نشده" else viewModel.mountOfUnitShare.addThousandsSeparator() + " متر"} ",
////                                                    style = MaterialTheme.typography.h3,
////                                                    color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
////                                                )
////                                            }
////                                        }
//
//                                        Spacer(modifier = Modifier.height(16.dp))
//                                    }
//                                }

                                /*
                            description
                            */

                                item {
                                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 12.dp)
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.description),
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                style = MaterialTheme.typography.h5.copy(
                                                    fontSize = 21.sp,
                                                ),
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = pdpDetail?.description ?: "",
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                style = MaterialTheme.typography.h4.copy(
                                                    fontSize = 16.sp
                                                ),
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))


                                        }


                                        //                                    Image(painter = painterResource(R.drawable.nd_edit_button),
                                        //                                        contentDescription = "Edit Description Clicked",
                                        //                                        modifier = Modifier
                                        //                                            .width((screen.screenWidthDp / 9).dp)
                                        //                                            .height((screen.screenWidthDp / 9).dp)
                                        //                                            .clickable {
                                        //                                                viewModel.onEditDescriptionClicked()
                                        //                                            })
                                    }
                                }


                                /*
                              location
                             */

                                item {
                                    Divider()
                                    Spacer(modifier = Modifier.size(12.dp))


                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 12.dp)
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.address),
                                                style = MaterialTheme.typography.h5.copy(
                                                    fontSize = 22.sp,
                                                ),
                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))


                                            Row(verticalAlignment = CenterVertically) {

                                                Image(
                                                    painter = painterResource(R.drawable.home_pin),
                                                    contentDescription = "location",
                                                    modifier = Modifier.size(30.dp),
                                                    colorFilter = ColorFilter.tint(Color.Gray)
                                                )
                                                Spacer(modifier = Modifier.size(8.dp))
                                                Text(
                                                    text = pdpDetail?.locationPhrase ?: "",
                                                    color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                                    style = MaterialTheme.typography.h4.copy(
                                                        fontSize = 18.sp
                                                    ),
                                                )

                                            }
                                            Spacer(modifier = Modifier.size(8.dp))
                                        }

                                        //
                                        //                                    Image(painter = painterResource(R.drawable.nd_edit_button),
                                        //                                        contentDescription = "Edit Address Clicked",
                                        //                                        modifier = Modifier
                                        //                                            .width((screen.screenWidthDp / 9).dp)
                                        //                                            .height((screen.screenWidthDp / 9).dp)
                                        //                                            .clickable {
                                        //                                                viewModel.onEditAddressClicked()
                                        //                                            })
                                    }

                                    Spacer(modifier = Modifier.size(20.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.size(70.dp))
//                                    Divider()
                                }

//
//                                item {
//                                    Divider()
//                                    Spacer(modifier = Modifier.size(12.dp))
//
//
//                                    Row(
//                                        modifier = Modifier.padding(horizontal = 8.dp),
//                                        verticalAlignment = Alignment.Top
//                                    ) {
//
//                                        Column(
//                                            modifier = Modifier
//                                                .weight(1f)
//                                                .padding(start = 12.dp)
//                                        ) {
//                                            Text(
//                                                text = stringResource(id = R.string.location_on_map),
//                                                style = MaterialTheme.typography.h5.copy(
//                                                    fontSize = 22.sp,
//                                                ),
//                                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
//                                            )
//
//                                            Spacer(modifier = Modifier.height(10.dp))
//
////                                            AndroidView(
////                                                modifier = Modifier.fillMaxSize(),
////                                                factory = { context ->
////                                                    MapView(context).apply {
////                                                        // Initialize MapView here
////                                                        // ...
////                                                    } as View // Cast MapView to View here
////                                                },
////                                                update = { mapView ->
////                                                    // mapView is now recognized as View, but you can cast it back to MapView if needed
////                                                    (mapView as? MapView)?.let {
////                                                        // Update the MapView here
////                                                    }
////                                                }
////                                            )
//
//                                            Spacer(modifier = Modifier.size(8.dp))
//                                        }
//
//                                        //
//                                        //                                    Image(painter = painterResource(R.drawable.nd_edit_button),
//                                        //                                        contentDescription = "Edit Address Clicked",
//                                        //                                        modifier = Modifier
//                                        //                                            .width((screen.screenWidthDp / 9).dp)
//                                        //                                            .height((screen.screenWidthDp / 9).dp)
//                                        //                                            .clickable {
//                                        //                                                viewModel.onEditAddressClicked()
//                                        //                                            })
//                                    }
//
//                                    Spacer(modifier = Modifier.size(20.dp))
//                                    Divider()
//                                }

                                /*
                              Agent
                             */

//                                item {
//
//                                    Spacer(modifier = Modifier.size(12.dp))
//
//                                    Row(
//                                        modifier = Modifier.padding(horizontal = 8.dp),
//                                        verticalAlignment = CenterVertically
//                                    ) {
//
//                                        Column(
//                                            modifier = Modifier
//                                                .weight(1f)
//                                                .padding(start = 12.dp)
//                                        ) {
//
//                                            Spacer(modifier = Modifier.height(10.dp))
//                                        }
//
//
//                                    }
//
//
//
//
//                                    Spacer(modifier = Modifier.size(25.dp))
//
//                                }

//                                //phone
//                                if (viewModel.agentPhone.isNotBlank()) {
//                                    item {
//                                        Column(
//                                            Modifier
//                                                .background(newGrey50)
//                                                .fillMaxWidth(),
//                                            verticalArrangement = Arrangement.Center,
//                                            horizontalAlignment = CenterHorizontally
//                                        ) {
//
//                                            Spacer(Modifier.height(15.dp))
//
//                                            Image(painter = painterResource(R.drawable.nd_phone),
//                                                contentDescription = "Phone Button",
//                                                modifier = Modifier
//                                                    .width((screen.screenWidthDp / 6).dp)
//                                                    .height((screen.screenWidthDp / 6).dp)
//                                                    .clickable {
//                                                        val intent = Intent(
//                                                            Intent.ACTION_DIAL,
//                                                            Uri.parse("tel:${viewModel.agentPhone}")
//                                                        )
//                                                        intent.flags =
//                                                            Intent.FLAG_ACTIVITY_NEW_TASK
//                                                        context.startActivity(intent)
//                                                    })
//
//                                            Spacer(Modifier.height(10.dp))
//
//                                            Text(
//                                                text = stringResource(id = R.string.phone),
//                                                style = MaterialTheme.typography.h3.copy(
//                                                    fontSize = 18.sp,
//                                                    color = newPrimaryTextColor
//                                                ),
//                                            )
//
//                                            Spacer(Modifier.height(10.dp))
//
//                                            Text(
//                                                text = viewModel.agentPhone,
//                                                style = MaterialTheme.typography.h3.copy(
//                                                    fontSize = 18.sp, color = newThirdTextColor
//                                                ),
//                                            )
//
//                                            Spacer(Modifier.height(15.dp))
//
//                                        }
//                                    }
//                                }
//
//
//                                //mobile
//                                if (viewModel.agentMobile.isNotBlank()) {
//                                    item {
//                                        Column(
//                                            Modifier
//                                                .background(newGrey50)
//                                                .fillMaxWidth(),
//                                            verticalArrangement = Arrangement.Center,
//                                            horizontalAlignment = CenterHorizontally
//                                        ) {
//
//                                            Spacer(Modifier.height(15.dp))
//
//                                            Image(painter = painterResource(R.drawable.nd_mobile),
//                                                contentDescription = "Mobile Button",
//                                                modifier = Modifier
//                                                    .width((screen.screenWidthDp / 6).dp)
//                                                    .height((screen.screenWidthDp / 6).dp)
//                                                    .clickable {
//                                                        val intent = Intent(
//                                                            Intent.ACTION_DIAL,
//                                                            Uri.parse("tel:${viewModel.agentMobile}")
//                                                        )
//                                                        intent.flags =
//                                                            Intent.FLAG_ACTIVITY_NEW_TASK
//                                                        context.startActivity(intent)
//
//                                                    })
//
//                                            Spacer(Modifier.height(10.dp))
//
//                                            Text(
//                                                text = stringResource(id = R.string.mobile),
//                                                style = MaterialTheme.typography.h3.copy(
//                                                    fontSize = 18.sp,
//                                                    color = newPrimaryTextColor
//                                                ),
//                                            )
//
//                                            Spacer(Modifier.height(10.dp))
//
//                                            Text(
//                                                text = viewModel.agentMobile,
//                                                style = MaterialTheme.typography.h3.copy(
//                                                    fontSize = 18.sp, color = newThirdTextColor
//                                                ),
//                                            )
//
//                                            Spacer(Modifier.height(15.dp))
//
//                                        }
//                                    }
//                                }
//
//
//                                //email
//                                if (viewModel.agentEmail.isNotBlank()) {
//                                    item {
//                                        Column(
//                                            Modifier
//                                                .background(newGrey50)
//                                                .fillMaxWidth(),
//                                            verticalArrangement = Arrangement.Center,
//                                            horizontalAlignment = CenterHorizontally
//                                        ) {
//
//                                            Spacer(Modifier.height(15.dp))
//
//                                            Image(painter = painterResource(R.drawable.nd_email),
//                                                contentDescription = "Email",
//                                                modifier = Modifier
//                                                    .width((screen.screenWidthDp / 6).dp)
//                                                    .height((screen.screenWidthDp / 6).dp)
//                                                    .clickable {
//                                                        val recipient = viewModel.agentEmail
//                                                        val subject = "Kilid, Email Subject"
//                                                        val message = "This is a test message."
//
//                                                        val intent =
//                                                            Intent(Intent.ACTION_SENDTO).apply {
//                                                                data =
//                                                                    Uri.parse("mailto:$recipient")
//                                                                putExtra(
//                                                                    Intent.EXTRA_SUBJECT,
//                                                                    subject
//                                                                )
//                                                                putExtra(
//                                                                    Intent.EXTRA_TEXT, message
//                                                                )
//                                                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                                            }
//
//                                                        context.startActivity(
//                                                            Intent.createChooser(
//                                                                intent, "Send email"
//                                                            )
//                                                        )
//                                                    })
//
//                                            Spacer(Modifier.height(10.dp))
//
//                                            Text(
//                                                text = stringResource(id = R.string.email),
//                                                style = MaterialTheme.typography.h3.copy(
//                                                    fontSize = 18.sp,
//                                                    color = newPrimaryTextColor
//                                                ),
//                                            )
//
//                                            Spacer(Modifier.height(10.dp))
//
//                                            Text(
//                                                text = viewModel.agentEmail,
//                                                style = MaterialTheme.typography.h3.copy(
//                                                    fontSize = 18.sp, color = newThirdTextColor
//                                                ),
//                                            )
//
//                                            Spacer(Modifier.height(15.dp))
//
//                                        }
//                                    }
//                                }


                            }
                            PullRefreshIndicator(
                                refreshing = pdpDetailUiState == UIState.Loading,
                                state = swipeRefreshState,
                                modifier = Modifier.align(Alignment.TopCenter),
                                backgroundColor = Color.White,
                                contentColor = kilidPrimaryColor
                            )
                            MobinButton(
                                title = "تماس با آگهی دهنده",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                    .height(55.dp)
                                    .align(Alignment.BottomCenter)
                                    .border(
                                        width = 1.dp,
                                        color = kilidPrimaryColor,
                                        shape = RoundedCornerShape(8.dp)
                                    ),

                                painter = rememberAsyncImagePainter(model = R.drawable.phone_enabled),
                                onClick = {
                                    coroutineScope.launch {
                                        identifier?.run {
                                            viewModel.onTriggerEvent(PdpScreenEvent.GetContactInformation(id = this))
                                        }
                                        viewModel.showBottomSheet(PdpBottomSheetType.CONTACT_AGENT)
                                        pdpModalBottomSheetState.show()
                                    }
                                }
                            )
                        }
                    }


                })
        }
    }
}
