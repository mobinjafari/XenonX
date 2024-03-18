package org.lotka.xenonx.presentation.ui.screens.plp.SingleChatScreen

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.wrapContentHeight

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.lotka.xenonx.R
import org.lotka.xenonx.domain.model.model.plp.PlpItemResultModel
import org.lotka.xenonx.presentation.composables.FastImage
import org.lotka.xenonx.presentation.composables.reusables.topbar.HomeTopBar
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.LockIconColor
import org.lotka.xenonx.presentation.theme.TelegramBackGround
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
import org.lotka.xenonx.presentation.util.UIState
import java.util.Collections

@Composable
fun SingleChatScreen(
    onBackPressed: () -> Unit,
    onToggleTheme: () -> Unit,
    isDarkMode: Boolean = false,
    viewModel: PlpViewModel,
    navController: NavController
) {
    val messages by viewModel.messages.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = TelegramBackGround),
        topBar = {
            SingleChatTopBar(
                onClick = {},
                mainScreens = true,
                onToggleTheme = {
                    onToggleTheme()
                },
                onBackPressed = onBackPressed,
                isDarkMode = isDarkMode,
                viewModel = viewModel
            )
        },
        bottomBar = {
            SingleChatBottomBar(viewModel)
        },
//        drawerElevation = 0.dp,
//        drawerGesturesEnabled = false,
//        drawerContentColor = Color.White,
//        drawerScrimColor = Color.White,
        content = { PadingValue ->
            LazyColumn(
                modifier = Modifier
                    .padding(PadingValue)

                    .fillMaxSize(),
                reverseLayout = true
            ) {
                items(messages.indices.toList()) { index ->
                    val message = viewModel.getMessageText(index)
                    message?.let {
                        Card(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .background(
                                    color = TelegramBackGround,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                                ),
                            backgroundColor = TelegramBackGround,
                            contentColor = TelegramBackGround,
                            elevation = 1.dp


                        ) {
                            Text (
                                text = it,
                                fontSize = 18.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(8.dp)

                            )
                                }



                    }
                }
            }
        }
    )
}
