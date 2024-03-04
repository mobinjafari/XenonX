package org.lotka.xenonx.domain.usecase.chat.chatScreen


import org.lotka.xenonx.domain.repository.auth.ChatScreenRepository

class LoadOpponentProfileFromFirebase(
    private val chatScreenRepository: ChatScreenRepository
) {
    suspend operator fun invoke(opponentUUID: String) =
        chatScreenRepository.loadOpponentProfileFromFirebase(opponentUUID)
}