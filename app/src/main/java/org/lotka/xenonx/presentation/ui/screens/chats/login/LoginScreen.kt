package org.lotka.xenonx.presentation.ui.screens.chats.login



import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.kilid.portal.presentation.composables.etc.TextFieldHeader
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.lotka.xenonx.presentation.composables.PasswordTextField
import kotlinx.coroutines.launch
import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.composables.etc.KilidTextField
import org.lotka.xenonx.presentation.composables.etc.MobinButton
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = viewModel.loginState.collectAsState(initial = null)

    val scaffoldState=rememberScaffoldState()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

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
                    title = { TextFieldHeader(text = "Login") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack,
                                contentDescription = "back",
                                modifier = Modifier.size(18.dp)
                                )
                        }
                    }, backgroundColor = Color.White
                )},
            content = {paddingValues ->
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
                            viewModel.email.value=newValue

                        }, value = viewModel.email.value,
                        label = R.string.emile1,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done)

                    Spacer(modifier = Modifier.height(8.dp))

                    PasswordTextField(
                        value = viewModel.password.value, label = R.string.password1,
                        onValueChange = {newvalue->
                            viewModel.password.value=newvalue
                        },
                        isError = false,
                        isErrorMessage = "")
                    Spacer(modifier = Modifier.height(24.dp))
                    MobinButton(title = "Login", onClick = {

                        viewModel.loginUser(
                            email = viewModel.email.value,
                            password = viewModel.password.value
                        )
                        viewModel.loginState
                            .onEach { state ->
                                if (state.isSuccess?.isNotEmpty() == true) {
                                    navController.navigate(HomeScreensNavigation.HomeChatScreen.route)
                                }
                            }
                            .launchIn(scope)

                    })








                }


            }
        )


    }
}