package org.lotka.xenonx.domain.model.model.chat

data class MessageRegister(
    var chatMessage: ChatMessage,
    var isMessageFromOpponent: Boolean
)