package com.example.chatwithme.domain.usecase.userListScreen

import org.lotka.xenonx.domain.repository.auth.UserListScreenRepository


class LoadPendingFriendRequestListFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke() =
        userListScreenRepository.loadPendingFriendRequestListFromFirebase()
}