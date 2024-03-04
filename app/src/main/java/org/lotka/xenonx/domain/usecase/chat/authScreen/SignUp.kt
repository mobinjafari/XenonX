package org.lotka.xenonx.domain.usecase.chat.authScreen

import org.lotka.xenonx.domain.repository.auth.AuthScreenRepository


class SignUp(
    private val authScreenRepository: AuthScreenRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authScreenRepository.signUp(email, password)
}