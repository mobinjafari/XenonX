package org.lotka.xenonx.presentation.ui.screens.plp.SingleChatScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.lotka.xenonx.R
import org.lotka.xenonx.presentation.composables.FastImage
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.OnlineTextColor
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel

@Composable
fun SingleChatTopBar(
    showAgency: Boolean = true,
    onClick: () -> Unit,
    mainScreens: Boolean = false,
    onBackPressed: () -> Unit,
    onToggleTheme: () -> Unit,
    isDarkMode: Boolean = false,
    viewModel: PlpViewModel,
    isOnline : Boolean = true
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Right,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(if (isDarkMode) kilidDarkBackgound else kilidWhiteBackgound)
            ) {

                Icon(
                    painter  = painterResource(id = R.drawable.arrow_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { onBackPressed() }
                        .size(16.dp)
                )
                Spacer(modifier = Modifier.width(23.dp))
                FastImage(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                    imageUrl = R.drawable.ic_info_light, // Consider using actual image URL
                    contentDescription = "Plp Item Image",
                    isRoundImage = true
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 11.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {

                    if (isOnline){

                        Text(text = "jahari",
                            style = KilidTypography.h5,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "online",
                            style = KilidTypography.h3,
                            color = OnlineTextColor)

                    }else{
                        Text(text = "jahari",
                            style = KilidTypography.h5,
                        )
                    }

                }
                Icon(
                    painter = painterResource(id = R.drawable.menu ),
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { onBackPressed() }
                        .size(16.dp)
                )


            }
        },
        backgroundColor = Color.White,
        elevation = 0.dp,
        contentColor = Color.Black, // Changed to contrast with white background
        modifier = Modifier.border(BorderStroke(0.dp, Color.White))
    )
}
