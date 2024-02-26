package com.kilid.portal.presentation.ui.screens.chats.register

data class RegisterState (
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)