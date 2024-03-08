package org.lotka.xenonx.domain.usecase.chat.authScreen

import javax.inject.Inject


data class AuthUseCases @Inject constructor(
    val isUserAuthenticated: IsUserAuthenticatedInFirebase,
    val signIn: SignIn,
    val signUp: SignUp,
)