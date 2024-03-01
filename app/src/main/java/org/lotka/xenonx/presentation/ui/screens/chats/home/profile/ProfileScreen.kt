package org.lotka.xenonx.presentation.ui.screens.chats.home.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kilid.portal.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.composables.FastImage
import org.lotka.xenonx.presentation.composables.HeaderText
import org.lotka.xenonx.presentation.composables.TextFieldHeader
import org.lotka.xenonx.presentation.ui.screens.chats.home.BottomNavigationItem
import org.lotka.xenonx.presentation.ui.screens.chats.home.BottomNavigationItemMenu

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController,
    onBack: () -> Unit,

) {
    val imageUrl = profileViewModel.userData.value?.imageUrl
    val name = profileViewModel.userData.value?.name
    val number = profileViewModel.userData.value?.number

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationItemMenu(
                selectItem = BottomNavigationItem.PROFILE,
                navController = navController
            )
        },
        topBar = {
            ProfileTopBar(onBackPressed = onBack, onSaveClick = {
                profileViewModel.createOrUpdateProfile(
                    name =name,
                    number = number,
                    imageUrl = imageUrl)
            })

        }, drawerElevation = 0.dp,
        drawerGesturesEnabled = false,
        drawerContentColor = Color.White,
        drawerScrimColor = Color.White,


        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                ProfileImage(imageUrl = imageUrl, viewModel = profileViewModel)

                Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp), verticalAlignment = Alignment.CenterVertically
                    ){
                    TextFieldHeader(text = "name", modifier = Modifier.width(100.dp))
                    TextField(
                        value = profileViewModel.userData.value?.name ?: "",
                        onValueChange = { profileViewModel.onNameTextFieldChanged(it) },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White,
                            cursorColor = Color.Black,
                        ),

                        )
                    Spacer(modifier = Modifier.height(20.dp))

                    TextFieldHeader(text = "Number", modifier = Modifier.width(100.dp))
                    TextField(
                        value = profileViewModel.userData.value?.number ?: "",
                        onValueChange = { profileViewModel.onTextFieldNumberChanged(it) },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White,
                            cursorColor = Color.Black,
                        ),

                        )
                    Spacer(modifier = Modifier.height(40.dp))

                    HeaderText(text = "LogOut", modifier = Modifier.clickable {
                       profileViewModel.LogoutUser()
                        navController.navigate(HomeScreensNavigation.LoginScreen.route)
                    }  )

                }


            }
        })
}


@Composable
fun ProfileImage(imageUrl: String?, viewModel: ProfileViewModel) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.uploadProfileImage(uri)
        }
    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                FastImage(
                    modifier = Modifier
                        .size(100.dp),
                    imageUrl = imageUrl,
                    contentDescription = "Plp Item Image",
                    isRoundImage = true, isProfilePicture = true
                )

            }
            TextFieldHeader(text = "Change Profile Picture", modifier = Modifier.clickable {

            })

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
                TextFieldHeader(text = "Back", modifier = Modifier.clickable {
                    onBackPressed()
                })
                Spacer(modifier = Modifier.width(2.dp))
                TextFieldHeader(text = "Save", modifier = Modifier.clickable {
                    onSaveClick()
                })
            }
        },
        backgroundColor = Color.White


    )
}