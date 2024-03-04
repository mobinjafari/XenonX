package org.lotka.xenonx.domain.usecase.chat.userListScreen

import com.example.chatwithme.domain.usecase.userListScreen.AcceptPendingFriendRequestToFirebase
import com.example.chatwithme.domain.usecase.userListScreen.CheckChatRoomExistedFromFirebase
import com.example.chatwithme.domain.usecase.userListScreen.CheckFriendListRegisterIsExistedFromFirebase
import com.example.chatwithme.domain.usecase.userListScreen.CreateChatRoomToFirebase
import com.example.chatwithme.domain.usecase.userListScreen.CreateFriendListRegisterToFirebase
import com.example.chatwithme.domain.usecase.userListScreen.LoadAcceptedFriendRequestListFromFirebase
import com.example.chatwithme.domain.usecase.userListScreen.LoadPendingFriendRequestListFromFirebase
import com.example.chatwithme.domain.usecase.userListScreen.RejectPendingFriendRequestToFirebase

data class UserListScreenUseCases(
    val acceptPendingFriendRequestToFirebase: AcceptPendingFriendRequestToFirebase,
    val checkChatRoomExistedFromFirebase: CheckChatRoomExistedFromFirebase,
    val checkFriendListRegisterIsExistedFromFirebase: CheckFriendListRegisterIsExistedFromFirebase,
    val createChatRoomToFirebase: CreateChatRoomToFirebase,
    val createFriendListRegisterToFirebase: CreateFriendListRegisterToFirebase,
    val loadAcceptedFriendRequestListFromFirebase: LoadAcceptedFriendRequestListFromFirebase,
    val loadPendingFriendRequestListFromFirebase: LoadPendingFriendRequestListFromFirebase,
    val openBlockedFriendToFirebase: OpenBlockedFriendToFirebase,
    val rejectPendingFriendRequestToFirebase: RejectPendingFriendRequestToFirebase,
    val searchUserFromFirebase: SearchUserFromFirebase,
)