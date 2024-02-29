package org.lotka.xenonx.presentation.ui.screens.chats.home.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.composables.FastImage
import org.lotka.xenonx.presentation.composables.HeaderText
import org.lotka.xenonx.presentation.ui.screens.chats.home.BottomNavigationItem
import org.lotka.xenonx.presentation.ui.screens.chats.home.BottomNavigationItemMenu

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    onBack: () -> Unit,
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationItemMenu(
                selectItem = BottomNavigationItem.PROFILE,
                navController = navController
            )
        },
        topBar = {
            ProfileTopBar(onBackPressed = onBack, onSaveClick = {})

        }, drawerElevation = 0.dp,
        drawerGesturesEnabled = false,
        drawerContentColor = Color.White,
        drawerScrimColor = Color.White,


        content = { paddingValues ->
            Column(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FastImage(
                    modifier = Modifier
                        .size(60.dp),
                    imageUrl = R.drawable.user,
                    contentDescription = "Plp Item Image",
                    isRoundImage = true
                )

                HeaderText(text = "Change Profile Picture", modifier = Modifier.clickable {

                })
                Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)



            }
        })
        }


@Composable
fun ProfileImage(imageUrl:String?,viewModel: ProfileViewModel){

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()){ uri ->
        uri?.let {

        }
    }




}






@Composable
fun ProfileTopBar(
    onSaveClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                HeaderText(text ="Back", modifier = Modifier.clickable {
                    onBackPressed()
                })
                HeaderText(text ="Save", modifier = Modifier.clickable {
                    onSaveClick()
                })
            }
        }


    )}