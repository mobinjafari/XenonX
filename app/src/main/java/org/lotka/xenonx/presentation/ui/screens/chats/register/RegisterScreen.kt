package org.lotka.xenonx.presentation.ui.screens.chats.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.kilid.portal.presentation.composables.etc.TextFieldHeader
import com.kilid.portal.presentation.ui.navigation.HomeScreensNavigation
import com.kilid.portal.presentation.ui.screens.chats.register.RegisterViewModel
import org.lotka.xenonx.presentation.composables.PasswordTextField
import kotlinx.coroutines.launch
import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.composables.etc.KilidTextField
import org.lotka.xenonx.presentation.composables.etc.MobinButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = viewModel.registerState.collectAsState(initial = null)

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val scaffoldState= rememberScaffoldState()

        LaunchedEffect(
            key1 = state.value?.isSuccess
        ) {
            scope.launch {
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    val succsess = state.value?.isSuccess
                    Toast.makeText(context, succsess, Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }

        LaunchedEffect(
            key1 = state.value?.isError
        ) {
            scope.launch {
                if (state.value?.isError?.isNotEmpty() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, error, Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }


        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { TextFieldHeader(text = "Register") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.back),
                                        modifier = Modifier.size(18.dp)
                            )
                        }
                    }, backgroundColor = Color.White
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    KilidTextField(
                        onTextChanged = {newValue->
                            viewModel.userName.value=newValue
                        },
                        value = viewModel.userName.value,
                        label = R.string.username,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done)

                    Spacer(modifier = Modifier.height(8.dp))

                    KilidTextField(
                        onTextChanged = {newValue->
                            viewModel.email.value=newValue
                        },
                        value = viewModel.email.value,
                        label = R.string.emile1,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done)

                    Spacer(modifier = Modifier.height(8.dp))

                    PasswordTextField(
                        value = viewModel.password.value,
                        label = R.string.password1,
                        onValueChange = {newValue->
                            viewModel.password.value=newValue
                        },
                        isError = false,
                        isErrorMessage = "")
                    Spacer(modifier = Modifier.height(24.dp))

                    MobinButton(title = "Register", onClick = {
                            viewModel.registerUser(
                            viewModel.userName.value,
                            viewModel.email.value,
                            viewModel.password.value)
                    })
                    Spacer(modifier = Modifier.height(12.dp))

                    Row (modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        if (state.value?.isLoading == true) {
                            CircularProgressIndicator()
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Column (horizontalAlignment = Alignment.CenterHorizontally){

                            TextFieldHeader(text = "already have an account?,Login",
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        HomeScreensNavigation.LoginScreen.route)})


                                   TextFieldHeader(text = "or continue with")
                        }

                                 Spacer(modifier = Modifier.height(12.dp))
                    }







                    }






            }
        )


    }
}