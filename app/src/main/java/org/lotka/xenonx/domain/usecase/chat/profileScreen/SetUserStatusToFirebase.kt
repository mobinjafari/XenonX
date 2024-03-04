package org.lotka.xenonx.domain.usecase.chat.profileScreen


import org.lotka.xenonx.domain.model.model.chat.UserStatus
import org.lotka.xenonx.domain.repository.auth.ProfileScreenRepository

class SetUserStatusToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(userStatus: UserStatus) =
        profileScreenRepository.setUserStatusToFirebase(userStatus)
}