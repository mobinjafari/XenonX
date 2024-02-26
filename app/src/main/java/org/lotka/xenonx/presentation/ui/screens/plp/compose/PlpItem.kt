package org.lotka.xenonx.presentation.ui.screens.plp.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lotka.xenonx.R
import org.lotka.xenonx.domain.enums.ListingType
import org.lotka.xenonx.domain.model.model.plp.PlpItemResultModel
import org.lotka.xenonx.presentation.composables.FastImage
import org.lotka.xenonx.presentation.theme.TelegramBackGroundColor
import org.lotka.xenonx.presentation.theme.TelegramColor
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.util.extension.addThousandsSeparator


@Composable
fun PlpItem(
    isDarkTheme: Boolean,
    item: PlpItemResultModel,
    screen: Configuration,
    onMoreClicked: (id: Int) -> Unit,
    onClicked: (id: Int) -> Unit,
    onLadderUpClick: (id: Int) -> Unit,
    onFeaturedClick: (id: Int) -> Unit,
    index: Int,

    ) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val isFeatured = item.isPremiumUser
        Card(
            modifier = Modifier
                .padding(4.dp)
                .background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)
                .clickable { onClicked(item.id) },
//        shape = RoundedCornerShape(8.dp) ,
//        border = BorderStroke( 1.dp  , if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders ),
            elevation = 0.dp
        ) {


            val thumbnailUrl: String = item.smallProfileImage ?: "string"
            val thumbnailDrawable: Int? =
                if (thumbnailUrl == "string") R.drawable.nd_noimage else null

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)
                    .padding(horizontal = 12.dp)
            ) {


                FastImage(
                    modifier = Modifier
                        .size(60.dp),
                    imageUrl = thumbnailUrl ?: thumbnailDrawable,
                    contentDescription = "Plp Item Image",
                    isRoundImage = true

                )


                // Advertisement Info
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .height(60.dp),
                    verticalArrangement = Arrangement.SpaceBetween,

                    ) {


                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp)

                    ) {


                        Text(
                            text = item.userFirstName ?: "نام ناشناخته",
                            style = MaterialTheme.typography.h4,
                            color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                        )


                        //price per meter or rent


                        Text(
                            text = item.lastMessageText ?: "نام ناشناخته",
                            style = MaterialTheme.typography.h3,
                            color = if (isDarkTheme) Color.White else Color.Gray,
                        )


                    }


                }

                Column(

                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ) {

                    Text(
                        text = item.lastMessageDate.toString(),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )


                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(shape = CircleShape)
                            .background(TelegramBackGroundColor),
                        Alignment.Center

                    ) {
                        Text(
                            text = item.numUnreadMessage.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                }


            }


        }
        Spacer(modifier = Modifier.height(12.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 80.dp)
        )


    }

}









