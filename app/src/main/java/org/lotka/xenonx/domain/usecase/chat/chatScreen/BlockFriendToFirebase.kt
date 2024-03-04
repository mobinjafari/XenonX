package org.lotka.xenonx.domain.usecase.chat.chatScreen

import org.lotka.xenonx.domain.repository.auth.ChatScreenRepository


class BlockFriendToFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        chatScreenRepository.blockFriendToFirebase(registerUUID)
}