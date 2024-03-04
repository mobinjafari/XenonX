package org.lotka.xenonx.domain.usecase.chat.profileScreen


import org.lotka.xenonx.domain.model.model.chat.User
import org.lotka.xenonx.domain.repository.auth.ProfileScreenRepository

class CreateOrUpdateProfileToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(user: User) =
        profileScreenRepository.createOrUpdateProfileToFirebase(user)
}