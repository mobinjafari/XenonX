package org.lotka.xenonx.presentation.ui.screens.chats.home

data class ChatData(
    val ChatId: String,
    val user1:ChatUser,
    val user2:ChatUser
)

data class ChatUser (
    val userId: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val number: String? = ""
)

