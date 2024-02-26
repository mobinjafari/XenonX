package org.lotka.xenonx.presentation.composables.reusables.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.theme.TelegramColor
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor


@Composable
fun HomeTopBar(
    showAgency: Boolean = true,
    onClick: () -> Unit,
    mainScreens: Boolean = false,
    onBackPressed: () -> Unit,
    onToggleTheme: () -> Unit,
    isDarkMode: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "agency",
            tint = Color.Black,
            modifier = Modifier
                .padding(start = 12.dp) // Add padding to the start
                .clickable {
                    onToggleTheme()
                }
        )
        Spacer(modifier = Modifier.weight(1f))

        if (mainScreens) {

            Text(
                text = "1 Story",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(end = 12.dp) // Add padding to the end
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
                    contentScale = ContentScale.Crop,
                    modifier = Modifier // Adjusted padding to center the image inside the box
                        .size(32.dp).padding(2.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                // Align the image to the center of the box
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "agency",
                tint = Color.Black,
                modifier = Modifier
                    .padding(end = 12.dp)
            )

        } else {
            Box(
                Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .align(CenterVertically)
                    .clickable {
                        onBackPressed()
                    },
                contentAlignment = Center
            ) {
                Spacer(modifier = Modifier.width(30.dp))
                Icon(
                    painter = rememberAsyncImagePainter(R.drawable.ic_arrow_left),
                    tint = Color.White,
                    contentDescription = "agency",
                    modifier = Modifier.size(18.dp)
                )
            }

        }
    }
}