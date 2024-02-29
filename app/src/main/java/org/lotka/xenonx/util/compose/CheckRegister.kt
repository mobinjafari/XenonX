package org.lotka.xenonx.util.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.ui.screens.chats.register.RegisterViewModel

@Composable
    fun CheckRegister(viewModel: RegisterViewModel, navController: NavController){
       val alreadyRegister= remember {
           mutableStateOf(false)
       }
          val registerIn = viewModel.registerIn.value
          if (registerIn && !alreadyRegister.value) {
              alreadyRegister.value = true
              navController.navigate(HomeScreensNavigation.HomeChatScreen.route)
          }
    }