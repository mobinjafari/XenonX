package org.lotka.xenonx.presentation.ui.screens.chats.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lotka.xenonx.presentation.composables.FastImage
import org.lotka.xenonx.presentation.composables.TextFieldHeader

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


data class Message(
    var sendBy:String?="",
    var message:String?="",
    var time:String?=""
)

data class Status (
    val user: ChatUser = ChatUser(),
    val imageUrl: String? = "",
    val message: String? = "",
    val time: String? = ""
)






@Composable
fun CommonRow(
    imageUrl: String?,
    name: String?,
    onItemClick:()->Unit
){
     Row (
         modifier = Modifier
             .fillMaxWidth()
             .height(75.dp)
             .clickable { onItemClick.invoke() },
         verticalAlignment = Alignment.CenterVertically
     ){
          FastImage(imageUrl = imageUrl, isRoundImage = true, isProfilePicture = false,
              modifier = Modifier.size(60.dp)
              )
         TextFieldHeader(text = name ?:"---",
             modifier = Modifier.padding(start = 4.dp)
             )
     }
}
fun NavigateTo(navController: NavController, Id: String) {
    // Navigate to the SingleChat screen with the provided chat ID
    navController.navigate("/$Id")
}
