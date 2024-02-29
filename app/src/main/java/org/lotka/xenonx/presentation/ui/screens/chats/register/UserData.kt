package org.lotka.xenonx.presentation.ui.screens.chats.register

data class UserData(
    val userId:String="",
    val name: String? ="",
    val email: String? ="",
    val number: String? ="",
    val imageUrl: String? ="",
){
    fun toMap(): Map<String, String?> {
        return mapOf("userId" to userId,"name" to name,"number" to number,"imageUrl" to imageUrl,"email" to email)
    }
}

