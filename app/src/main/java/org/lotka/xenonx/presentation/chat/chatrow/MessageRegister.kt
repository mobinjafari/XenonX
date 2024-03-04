package org.lotka.xenonx.presentation.chat.chatrow

import org.lotka.xenonx.domain.model.model.chat.ChatMessage

data class MessageRegister(
    var chatMessage: ChatMessage,
    var isMessageFromOpponent: Boolean
)