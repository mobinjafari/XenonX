package com.example.chatwithme.domain.usecase.userListScreen

import org.lotka.xenonx.domain.repository.auth.UserListScreenRepository


class AcceptPendingFriendRequestToFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        userListScreenRepository.acceptPendingFriendRequestToFirebase(registerUUID)
}