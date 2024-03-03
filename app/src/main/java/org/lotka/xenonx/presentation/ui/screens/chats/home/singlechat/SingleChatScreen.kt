package org.lotka.xenonx.presentation.ui.screens.chats.home.singlechat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lotka.xenonx.presentation.composables.FastImage
import org.lotka.xenonx.presentation.composables.HeaderText
import org.lotka.xenonx.presentation.composables.TextFieldHeader
import org.lotka.xenonx.presentation.ui.screens.chats.home.Message
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel

@Composable
fun SingleChatScreen(
    navController: NavController,
    plpViewModel: PlpViewModel,
    chatId: String
) {
    var replay by rememberSaveable {
        mutableStateOf("")
    }
    val onSendReplayText = {
        plpViewModel.onSendReplay(replay, chatId)
        replay = ""
    }
    val myUser = plpViewModel.userData.value
    val currenChat = plpViewModel.chats.value.first({ it.ChatId == chatId })
    val chatUser = if (myUser?.userId == currenChat.user1.userId) currenChat.user2 else currenChat.user1

    LaunchedEffect(key1 = Unit){
        plpViewModel.popularMessages(chatId)
    }
    BackHandler {
     plpViewModel.depopulateMessages()
    }



    Column(modifier = Modifier.fillMaxWidth()) {
        chatHeader(name = chatUser.name?:"",
            imageUrl = chatUser.imageUrl?:"", ) {



        }
        TextFieldHeader(text = plpViewModel.chatMessages.toString())

        MessageBox(modifier = Modifier.weight(1f),
            chatmessage = plpViewModel.chatMessages,
            currentUserId = myUser?.userId?:"")


        ReplayBox(
            replay = replay, onReplayChange = { replay = it },
            onSendReplayText = onSendReplayText
        )

    }

}

@Composable
fun MessageBox(
    modifier: Modifier,
    chatmessage:List<Message>,
    currentUserId:String
){
    LazyColumn(modifier = modifier) {



        items(chatmessage) { message ->
            val alignment = if (message.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (message.sendBy == currentUserId) Color(0xFF0F9D58) else Color(0xFFDB4437)

            Column (modifier = modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
                ){

            }

            Text(text = "${message.sendBy}: ${message.message}", modifier = Modifier.clip(
                RoundedCornerShape(8.dp)
            ).background(color)
            , color = Color.White,
                fontWeight = FontWeight.Bold

            )



        }
    }
}


@Composable
fun chatHeader(
    name: String,
    imageUrl: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        ) {
        Icon(Icons.Default.ArrowBack, contentDescription =null, modifier = Modifier
            .clickable {
                onBackClick.invoke()
            }
            .padding(8.dp) )
        FastImage(imageUrl = imageUrl,
            isRoundImage = true, modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
            ,)
           TextFieldHeader(text = name)

    }
}


@Composable
fun ReplayBox(
    replay: String,
    onReplayChange: (String) -> Unit,
    onSendReplayText: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(value = replay, onValueChange = onReplayChange, maxLines = 3)
            Button(onClick = { onSendReplayText.invoke() }) {
                Text(text = "Send")
            }
        }
    }

}