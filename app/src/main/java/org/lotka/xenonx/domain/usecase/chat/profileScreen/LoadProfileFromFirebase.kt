package org.lotka.xenonx.domain.usecase.chat.profileScreen


import org.lotka.xenonx.domain.repository.auth.ProfileScreenRepository

class LoadProfileFromFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke() = profileScreenRepository.loadProfileFromFirebase()
}