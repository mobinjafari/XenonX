package org.lotka.xenonx.presentation.ui.screens.plp.compose

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lotka.xenonx.R
import org.lotka.xenonx.domain.enums.ListingType
import org.lotka.xenonx.domain.model.model.plp.PlpItemResultModel
import org.lotka.xenonx.presentation.composables.FastImage
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidDarkBorders
import org.lotka.xenonx.presentation.theme.kilidDarkObjects
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteBorders
import org.lotka.xenonx.presentation.theme.kilidWhiteObjects
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.presentation.util.convertToMillis
import org.lotka.xenonx.presentation.util.timeAgoInPersian
import org.lotka.xenonx.util.extension.addMeter
import org.lotka.xenonx.util.extension.addThousandsSeparator

@Composable
fun PlpItem(
    isDarkTheme : Boolean,
    item: PlpItemResultModel,
    screen: Configuration,
    onMoreClicked : (id:Int) -> Unit,
    onClicked : (id:Int) -> Unit,
    onLadderUpClick : (id:Int) -> Unit,
    onFeaturedClick : (id:Int) -> Unit,
    index : Int
) {

    val isFeatured = item.featured
    Card(
        modifier = Modifier
            .padding(4.dp)
            .background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)
            .clickable { onClicked(item.id) },
        shape = RoundedCornerShape(8.dp) ,
        border = BorderStroke( 1.dp  , if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders ),
        elevation = 0.dp) {


        val thumbnailUrl: String = item.coverPicture ?: "string"
        val thumbnailDrawable: Int? = if (thumbnailUrl == "string") R.drawable.nd_noimage else null

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)
        ) {


            Box(
                modifier = Modifier
                    .background(Color.White)
                    .size(170.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                // Image
                FastImage(
                    imageUrl = thumbnailUrl ?: thumbnailDrawable,
                    contentDescription = "Plp Item Image",
                    modifier = Modifier
                        .background(Color.White)
                        .size(170.dp)
                )

                // Text
                if (item.featured == true){
                    Text(
                        text = "آگهی ویژه",
                        style = KilidTypography.h5.copy(fontSize = 16.sp , color = Color.White),
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.6f))
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .alpha(0.6f)
                        ,                    textAlign = TextAlign.Center
                    )
                }
            }

            // Advertisement Info
            Column(
                modifier = Modifier
                    .weight(2f)
                    .height(170.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {

                //title
                Text(
                    text = item.title ?: "",
                    style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                    color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 5.dp , start = 8.dp , end = 2.dp, bottom = 0.dp)
                )

                Column(Modifier.padding(top = 0.dp , start = 8.dp , end = 2.dp, bottom = 0.dp)) {

                    //price or deposit text
                    val text : String = when (item.listingType){
                        ListingType.SALE_AND_PRE_SALE -> {


                            stringResource(id = R.string.total_price)
                                .plus(" : ")
                                .plus(
                                    if (item.agreedPrice == true) {
                                        stringResource(id = R.string.agreed_price)
                                    } else if (item.priceOrDeposit?.toInt() == 0) {
                                        "رایگان"
                                    } else {
                                        item.priceOrDeposit?.toBigDecimal()?.toPlainString()
                                            ?.addThousandsSeparator() + " تومان"
                                    }
                                )


                        }
                        ListingType.MORTGAGE_AND_RENT -> {


                            stringResource(id = R.string.mortgage)
                                .plus(": ")
                                .plus(
                                    if (item.agreedPrice == true) {
                                        stringResource(id = R.string.agreed_price)
                                    } else {
                                        item.priceOrDeposit?.toBigDecimal()?.toPlainString()
                                            ?.addThousandsSeparator() + " تومان"
                                    }
                                )

                        }

                        else -> {
                            ""
                        }
                    }

                    Text(
                        text = text,
                        style = MaterialTheme.typography.h4,
                        color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                    )



                    //price per meter or rent


                    //total price or deposit
                    if (item.listingType == ListingType.SALE_AND_PRE_SALE)
                    {
                        if (item.unitPriceOrRent != null) {

                            Text(
                                text = stringResource(id = R.string.square_meters)
                                    .plus(" : ")
                                    .plus(
                                        if (item.unitPriceOrRent == 0L) "توافقی" else item.unitPriceOrRent?.toBigDecimal()
                                            ?.toPlainString()?.addThousandsSeparator() + " تومان"
                                    ),
                                style = MaterialTheme.typography.h3,
                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                            )
                        }

                    }
                    else {

                        if (item.unitPriceOrRent != null) {

                            Text(
                                text = stringResource(id = R.string.rent)
                                    .plus(" : ")
                                    .plus(
                                        if (item.agreedPrice == true) {
                                            stringResource(id = R.string.agreed_price)
                                        } else if (item.unitPriceOrRent?.toInt() == 0) {
                                            "رایگان"
                                        } else {
                                            item.unitPriceOrRent?.toBigDecimal()?.toPlainString()
                                                ?.addThousandsSeparator() + " تومان"
                                        }
                                    ),
                                style = MaterialTheme.typography.h3,
                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                            )

                        }


                    }


                }


                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, end = 4.dp, start = 4.dp, top = 0.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Floor Area Pill
                        Text(
                            textAlign = TextAlign.Center,
                            text = item.floorArea?.toString()?.replace(".0", "")?.plus(" ")?.addMeter()
                                ?: "",
                            style = KilidTypography.h3.copy(
                                color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                fontSize = 14.sp,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .background(
                                    if (isDarkTheme) kilidDarkObjects else kilidWhiteObjects,
                                    RoundedCornerShape(25.dp)
                                )
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                                .align(Alignment.CenterVertically),
                        )


                        if (item.numBeds != null){
                            if (item.numBeds?.toString()?.isNotBlank() == true) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    textAlign = TextAlign.Center,
                                    text = (if (item.numBeds.toString() == "0") "بدون" else item.numBeds).toString() + " " + stringResource(
                                        id = R.string.bed_room
                                    ),
                                    style = KilidTypography.h3.copy(
                                        color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        fontSize = 14.sp,
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .background(
                                            if (isDarkTheme) kilidDarkObjects else kilidWhiteObjects,
                                            RoundedCornerShape(25.dp)
                                        )
                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                        .align(Alignment.CenterVertically),
                                )
                            }
                        }



                        // Number of Bedroom Pill


                        Spacer(modifier = Modifier.width(8.dp))

                        if (item.landUseType!=null && item.landUseType?.nameFa?.isNotBlank() == true){
                            Text(
                                textAlign = TextAlign.Center,
                                text = item.landUseType?.nameFa?:"",
                                style = KilidTypography.h3.copy(
                                    color = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                    fontSize = 14.sp,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .background(
                                        if (isDarkTheme) kilidDarkObjects else kilidWhiteObjects,
                                        RoundedCornerShape(25.dp)
                                    )
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                                    .align(Alignment.CenterVertically),
                            )
                        }



                    }


                    var time :String= timeAgoInPersian(convertToMillis(item.searchDate.toString()))
                    var location = item.locationPhrase
                    //title
                    Text(
                        text =  time.plus(" در ").plus(location),
                        style = MaterialTheme.typography.h2.copy(fontSize = 13.sp),
                        color = if (isDarkTheme) kilidDarkTexts.copy(alpha = 0.5f) else kilidWhiteTexts,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 0.dp , start = 8.dp , end = 2.dp, bottom = 6.dp)
                    )
                }

            }







        }


    }
}









