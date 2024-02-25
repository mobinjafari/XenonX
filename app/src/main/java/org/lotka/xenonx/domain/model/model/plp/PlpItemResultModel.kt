package org.lotka.xenonx.domain.model.model.plp


import androidx.annotation.Keep

@Keep
data class PlpItemResultModel(
    var id: Int,
    var userFirstName : String?,
    var userLastName: String?,
    var smallProfileImage: String?,
    var lastMessageText : String?,
    var lastMessageDate : String?,
    var numUnreadMessage: Int?,
    var lastTypingDate: String?,
    var isPremiumUser: Boolean?,
    var lastMessageType : String?,
    var lastChatSeenDate: Long?,
    var lastMessageStatus : String?,
    var hasStory : Boolean?,
    )