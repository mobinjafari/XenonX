package org.lotka.xenonx.domain.usecase.chat.userListScreen

import org.lotka.xenonx.domain.repository.auth.UserListScreenRepository


class SearchUserFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(userEmail: String) =
        userListScreenRepository.searchUserFromFirebase(userEmail)
}