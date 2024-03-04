package org.lotka.xenonx.domain.usecase.chat.authScreen

import org.lotka.xenonx.domain.repository.auth.AuthScreenRepository


class IsUserAuthenticatedInFirebase(
    private val authScreenRepository: AuthScreenRepository
) {
    operator fun invoke() = authScreenRepository.isUserAuthenticatedInFirebase()
}