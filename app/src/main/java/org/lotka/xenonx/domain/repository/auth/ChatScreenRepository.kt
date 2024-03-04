package org.lotka.xenonx.domain.repository.auth


import kotlinx.coroutines.flow.Flow
import org.lotka.xenonx.data.util.Response
import org.lotka.xenonx.domain.model.model.chat.ChatMessage
import org.lotka.xenonx.domain.model.model.chat.User

interface ChatScreenRepository {
    suspend fun insertMessageToFirebase(
        chatRoomUUID: String,
        messageContent: String,
        registerUUID: String,
        oneSignalUserId: String
    ): Flow<Response<Boolean>>

    suspend fun loadMessagesFromFirebase(
        chatRoomUUID: String,
        opponentUUID: String,
        registerUUID: String
    ): Flow<Response<List<ChatMessage>>>

    suspend fun loadOpponentProfileFromFirebase(opponentUUID: String): Flow<Response<User>>
    suspend fun blockFriendToFirebase(registerUUID: String): Flow<Response<Boolean>>
}