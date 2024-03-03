package org.lotka.xenonx.presentation.ui.screens.chats.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lotka.xenonx.presentation.composables.HeaderText
import org.lotka.xenonx.presentation.composables.TextFieldHeader
import org.lotka.xenonx.presentation.ui.navigation.HomeScreensNavigation
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel

@Composable
fun StatusScreen(navController: NavController, plpViewModel: PlpViewModel) {
    val inProssess = plpViewModel.inProssessStatus.value
    if (inProssess) {
        CircularProgressBar()
    } else {
        val status = plpViewModel.status.value
        val userData = plpViewModel.userData.value
        val myStatus = status.filter {
            it.user.userId == userData?.userId
        }
        val otherStatus = status.filter {
            it.user.userId != userData?.userId
        }
        
        val luncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()){uri ->
            uri.let {
                if (uri != null) {
                    plpViewModel.uploadstatus(uri)
                }
            }

        }

        Scaffold(
            floatingActionButton = {
                FABButton(
                    onFabClick = {
                        luncher.launch("image/*")
                    }
                )
            }, content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    HeaderText(text = "status")
                    if (status.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            TextFieldHeader(text = "No status found")
                        }
                        if (myStatus.isNotEmpty()) {
                            CommonRow(
                                imageUrl = myStatus[0].user.imageUrl,
                                name = myStatus[0].user.name
                            ) {

                                NavigateTo(navController, HomeScreensNavigation.Setting.route)

                            }
                            Divider()
                            var uniqueUsers = otherStatus.map {it.user}
                                .toSet().toList()
                            LazyColumn(modifier = Modifier.weight(1f)){
                                items(uniqueUsers){user->
                                    CommonRow(imageUrl = user.imageUrl, name = user.name) {
//                                        NavigateTo(navController,HomeScreensNavigation.createRout(user.userId))
                                    }       

                                }
                            }


                        }
                    }

                }
            }
        )

        BottomNavigationItemMenu(
            selectItem = BottomNavigationItem.SETTINGS,
            navController = navController
        )
    }


}

@Composable
fun FABButton(
    onFabClick: () -> Unit,

    ) {

    FloatingActionButton(
        onClick = { onFabClick.invoke() },
        shape = CircleShape, modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit",
            tint = Color.White
        )

    }

}


@Composable
fun CircularProgressBar() {
    val progressColor = MaterialTheme.colors.primary
    val backgroundColor = Color.LightGray
    val strokeWidth = 8.dp
    val radius = 40.dp

    Box(
        modifier = Modifier
            .size(radius * 2)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

        }
    }
}