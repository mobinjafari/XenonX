package org.lotka.xenonx.domain.model.model.plp


import androidx.annotation.Keep
import org.lotka.xenonx.domain.enums.UserVerificationStatus

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
    var isItemPinned : Boolean = false,
    var verificationStatus : UserVerificationStatus = UserVerificationStatus.NONE,
    var isSilent: Boolean = false,
    var isTyping: Boolean = false,
    var isLockAccount: Boolean = false

    )