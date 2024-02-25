package org.lotka.xenonx.presentation.ui.screens.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.White
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor
import org.lotka.xenonx.presentation.ui.navigation.BackPressHandler
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.util.extension.toFarsi
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SplashScreen(
    navController: NavController, viewModel: SplashViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    var canPop by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current


    //custom back pressed handler
    val onBack = {

    }
    BackPressHandler(onBackPressed = onBack)



    LaunchedEffect("events") {

        viewModel.events.collectLatest { event ->
            when (event) {
                SplashEvent.NavigateToListing -> navController.navigate(HomeScreensNavigation.plp.route)

                else -> {}
        }
        }

    }



    navController.addOnDestinationChangedListener { controller, _, _ ->
        canPop = controller.previousBackStackEntry != null
    }


    Scaffold {
        Box(modifier = Modifier
            .background(kilidPrimaryColor)
            .fillMaxSize()
            .clickable {

            }) {


            Image(
                painter = painterResource(id = R.drawable.kilid_portal_logo),
                contentDescription = "Kilid",
                modifier = Modifier
                    .width(200.dp)
                    .height(250.dp)
                    .align(androidx.compose.ui.Alignment.Center),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier

                    .height(64.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.version).plus(" ").plus(
                        LocalContext.current.packageManager.getPackageInfo(
                            LocalContext.current.packageName, 0
                        ).versionName.toFarsi()
                    ),
                    style = KilidTypography.h3.copy(fontSize = 15.sp, color = White),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                LinearProgressIndicator(
                    color = White, modifier = Modifier
                        .width(64.dp)
                        .padding(vertical = 12.dp)
                )
            }

        }
    }
}


