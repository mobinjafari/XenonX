package com.kilid.portal.presentation.ui.screens.chats.login


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.kilid.portal.domain.usecase.auth.LoginUserUseCase

import com.kilid.portal.presentation.ui.navigation.HomeScreensNavigation
import com.kilid.portal.presentation.ui.screens.chats.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.lotka.xenonx.domain.util.ResultState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val registerUseCase: LoginUserUseCase
) : ViewModel() {



    var email = mutableStateOf("")
    var password = mutableStateOf("")


    private val _loginState = Channel<RegisterState>()
       val loginState = _loginState.receiveAsFlow()

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        registerUseCase.invoke(email, password).collect { result ->
            when (result) {
                is ResultState.Error -> {
                    _loginState.send(RegisterState(isError = "Sign In Failed"))
                }
                is ResultState.Loading -> {
                    _loginState.send(RegisterState(isLoading = true))
                }
                is ResultState.Success -> {
                    _loginState.send(RegisterState(isSuccess = "Sign In Success"))

                }

                else -> {}
            }
        }
    }


}