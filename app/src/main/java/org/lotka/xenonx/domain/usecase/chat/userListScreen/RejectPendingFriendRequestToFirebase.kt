package com.example.chatwithme.domain.usecase.userListScreen

import org.lotka.xenonx.domain.repository.auth.UserListScreenRepository


class RejectPendingFriendRequestToFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        userListScreenRepository.rejectPendingFriendRequestToFirebase(registerUUID)
}