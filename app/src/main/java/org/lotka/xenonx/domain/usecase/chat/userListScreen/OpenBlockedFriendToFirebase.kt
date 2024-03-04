package org.lotka.xenonx.domain.usecase.chat.userListScreen

import org.lotka.xenonx.domain.repository.auth.UserListScreenRepository


class OpenBlockedFriendToFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        userListScreenRepository.openBlockedFriendToFirebase(registerUUID)
}