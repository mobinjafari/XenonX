package org.lotka.xenonx.presentation.ui.screens.chats.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation

enum class BottomNavigationItem(val icon:Int ,val navDestination: HomeScreensNavigation){
    CHATLIST(R.drawable.chat,HomeScreensNavigation.HomeChatScreen),
    PROFILE(R.drawable.user,HomeScreensNavigation.ProfileScreen),
    SETTINGS(R.drawable.settings,HomeScreensNavigation.Setting),
}

@Composable
fun BottomNavigationItemMenu(
    selectItem: BottomNavigationItem,
    navController: NavController
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp)
            .background(color = Color.White)
    ){
        for (item in BottomNavigationItem.values()){
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .weight(1f)
                    .clickable {
                        navController.navigate(item.navDestination.route)
                    },
                colorFilter = if (item == selectItem) {
                    ColorFilter.tint(color = Color.Black)
                } else {
                    ColorFilter.tint(color = Color.LightGray)
                }
            )
        }
    }
}
