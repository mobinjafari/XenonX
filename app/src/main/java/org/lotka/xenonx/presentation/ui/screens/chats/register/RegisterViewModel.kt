package com.kilid.portal.presentation.ui.screens.chats.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.lotka.xenonx.domain.usecase.auth.RegisterUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.lotka.xenonx.domain.util.ResultState
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loginUserUseCase: RegisterUseCase
) : ViewModel() {

    var email = mutableStateOf("")
    var userName = mutableStateOf("")
    var password = mutableStateOf("")



    private val _registerState = Channel<RegisterState>()
    val registerState = _registerState.receiveAsFlow()



    fun registerUser(userName: String, email: String, password: String) =viewModelScope.launch {

        loginUserUseCase.invoke(userName,email, password).collect { result ->
            when (result) {
                is ResultState.Error -> {

                    _registerState.send(RegisterState(isError = "Sign Up Failed ${result.error?.error}"))
                }
                is ResultState.Loading -> {
                    _registerState.send(RegisterState(isLoading = true))
                }
                is ResultState.Success -> {
                    _registerState.send(RegisterState(isSuccess = "Sign Up Success"))
                }

                else -> {}
            }
    }
}

}