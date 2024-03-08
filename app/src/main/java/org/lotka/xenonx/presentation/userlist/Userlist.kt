package org.lotka.xenonx.presentation.userlist

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.example.chatwithme.presentation.bottomnavigation.BottomNavItem
import org.lotka.xenonx.presentation.profile.components.ProfileAppBar
import org.lotka.xenonx.presentation.userlist.components.AcceptPendingRequestList
import com.example.chatwithme.presentation.userlist.components.PendingFriendRequestList
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.lotka.xenonx.core.SnackbarController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Userlist(
    userListViewModel: UserListViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    keyboardController: SoftwareKeyboardController
) {
    val toastMessage = userListViewModel.toastMessage.value
    LaunchedEffect(key1 = toastMessage) {
        if (toastMessage != "") {
            SnackbarController(this).showSnackbar(snackbarHostState, toastMessage, "Close")
        }
    }
    var chatRoomUUID: String? by remember { mutableStateOf(null) }
    var opponentUUID: String? by remember { mutableStateOf(null) }
    var oneSignalUserId: String? by remember { mutableStateOf(null) }
    var registerUUID: String? by remember { mutableStateOf(null) }
//    if (chatRoomUUID != null) {
//        LaunchedEffect(key1 = Unit) {
//            navController.popBackStack()
//            navController.navigate(BottomNavItem.Chat.screen_route)
//        }
//    }
    if (chatRoomUUID != null && opponentUUID != null && registerUUID != null && oneSignalUserId != null) {
        LaunchedEffect(key1 = Unit) {
            navController.popBackStack()
            navController.navigate(BottomNavItem.Chat.screen_route + "/${chatRoomUUID}" + "/${opponentUUID}" + "/${registerUUID}" + "/${oneSignalUserId}")
        }
    }

    LaunchedEffect(key1 = Unit) {
        userListViewModel.refreshingFriendList()
    }
    val acceptedFriendRequestList = userListViewModel.acceptedFriendRequestList
    val pendingFriendRequestList = userListViewModel.pendingFriendRequestList

//    var showAlertDialog by remember {
//        mutableStateOf(false)
//    }
//    if (showAlertDialog) {
//        AlertDialogChat(
//            onDismiss = { showAlertDialog = !showAlertDialog },
//            onConfirm = {
//                showAlertDialog = !showAlertDialog
//                userListViewModel.createFriendshipRegisterToFirebase(it)
//            })
//    }

    val scrollState = rememberLazyListState()
    var isRefreshing by remember { userListViewModel.isRefreshing }

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
            userListViewModel.refreshingFriendList()
        },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 8.dp),
                state = state,
                refreshTriggerDistance = trigger,
                fade = true,
                scale = true,
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        Column(
            modifier = Modifier
                .focusable()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { keyboardController.hide() })
                }
        ) {
            ProfileAppBar()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
//                .weight(1f),
                state = scrollState,
            ) {
                items(acceptedFriendRequestList.value) { item ->
                    AcceptPendingRequestList(item) {
                        chatRoomUUID = item.chatRoomUUID
                        registerUUID = item.registerUUID
                        opponentUUID = item.userUUID
                        oneSignalUserId = item.oneSignalUserId
                    }
                }
                items(pendingFriendRequestList.value) { item ->
                    PendingFriendRequestList(item, {
                        userListViewModel.acceptPendingFriendRequestToFirebase(item.registerUUID)
                        userListViewModel.refreshingFriendList()
                    }, {
                        userListViewModel.cancelPendingFriendRequestToFirebase(item.registerUUID)
                        userListViewModel.refreshingFriendList()
                    })
                }
            }
        }
    }
}