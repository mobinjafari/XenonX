package org.lotka.xenonx.presentation.ui.screens.chats.welcome

import com.kilid.portal.presentation.composables.etc.HeaderText



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.kilid.portal.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.composables.etc.MobinButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WelcomeScreen(navController: NavController) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {



            HeaderText(text = "Welcome")
            Spacer(modifier = Modifier.height(20.dp))
            MobinButton(title = "Register", onClick = { navController.navigate(HomeScreensNavigation.RegisterScreen.route) })
            Spacer(modifier = Modifier.height(16.dp))
            MobinButton(title = "Login", onClick = { navController.navigate(HomeScreensNavigation.LoginScreen.route)})
        }

    }
}