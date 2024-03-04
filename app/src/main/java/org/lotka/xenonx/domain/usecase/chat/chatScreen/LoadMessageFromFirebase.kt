package org.lotka.xenonx.domain.usecase.chat.chatScreen

import org.lotka.xenonx.domain.repository.auth.ChatScreenRepository


class LoadMessageFromFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(
        chatRoomUUID: String,
        opponentUUID: String,
        registerUUID: String
    ) = chatScreenRepository.loadMessagesFromFirebase(chatRoomUUID, opponentUUID, registerUUID)
}