package org.lotka.xenonx.domain.usecase.chat.authScreen


import org.lotka.xenonx.domain.repository.auth.AuthScreenRepository

class SignIn(
    private val authScreenRepository: AuthScreenRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authScreenRepository.signIn(email, password)
}