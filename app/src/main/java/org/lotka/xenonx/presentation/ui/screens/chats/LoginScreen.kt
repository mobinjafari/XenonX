package com.kilid.portal.presentation.ui.screens.chats



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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lotka.xenonx.presentation.composables.PasswordTextField

import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.composables.etc.KilidTextField
import org.lotka.xenonx.presentation.composables.etc.MobinButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavController) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            KilidTextField(
                onTextChanged = {}, value = "",
                label = R.string.username,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done)

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(
                value = "", label = R.string.password1,
                onValueChange = {},
                isError = false,
                isErrorMessage = "")
            Spacer(modifier = Modifier.height(18.dp))
            MobinButton(title = "Login", onClick = {})
        }

    }
}