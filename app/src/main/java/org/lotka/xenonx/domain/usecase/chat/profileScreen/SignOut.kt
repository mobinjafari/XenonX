package com.example.chatwithme.domain.usecase.profileScreen

import org.lotka.xenonx.domain.repository.auth.ProfileScreenRepository


class SignOut(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke() = profileScreenRepository.signOut()
}