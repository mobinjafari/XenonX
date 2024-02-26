package org.lotka.xenonx.presentation.composables.reusables.topbar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lotka.xenonx.R

@Composable
fun HomeTopBar(
    showAgency: Boolean = true,
    onClick: () -> Unit,
    mainScreens: Boolean = false,
    onBackPressed: () -> Unit,
    onToggleTheme: () -> Unit,
    isDarkMode: Boolean = false
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black,
                    modifier = Modifier
                        .clickable {
//                            onToggleTheme()
                        }
                )
                Spacer(modifier = Modifier.weight(1f))
                if (mainScreens) {
                    Text(
                        text = "1 Story",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .align(Alignment.CenterVertically)
                            .border(1.dp, Color.Gray, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.telegram),
                            contentDescription = "Telegram Logo",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(2.dp)
                                .clip(CircleShape)
                                .align(Alignment.Center),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.Black,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                } else {
                    Box(
                        Modifier
                            .width(80.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterVertically)
                            .clickable { onBackPressed() },
                        contentAlignment = Alignment.Center
                    ) {
                        Spacer(modifier = Modifier.width(30.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        },
        backgroundColor = Color.White,
        elevation = 0.dp,
        contentColor = Color.White,
        modifier = Modifier.border(BorderStroke(0.dp, Color.White))



    )
}
