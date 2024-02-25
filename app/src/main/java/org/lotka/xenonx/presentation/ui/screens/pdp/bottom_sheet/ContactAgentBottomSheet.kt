package org.lotka.xenonx.presentation.ui.screens.pdp.bottom_sheet

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.lotka.xenonx.R
import org.lotka.xenonx.domain.model.model.contactInfo.ContactInformation
import org.lotka.xenonx.presentation.composables.etc.MobinButton
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.White
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor
import org.lotka.xenonx.presentation.theme.newSecondaryColor
import org.lotka.xenonx.presentation.util.UIState

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ContactAgentBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    contactInformation: ContactInformation ?,
    contactInformationUIState: UIState,
    identifier : Int,
) {
    val context = LocalContext.current


    val spaceBetweenButtons = 15.dp

    Column(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Divider(
            Modifier
                .width(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.CenterHorizontally), color = newSecondaryColor, thickness = 8.dp
        )


        Spacer(modifier = Modifier.height(spaceBetweenButtons*2))


        Image(
            painter = rememberAsyncImagePainter(model = contactInformation?.departmentLogoUrl),
            contentDescription = "",
            modifier = Modifier
                .align(CenterHorizontally)
                .width(100.dp)
                .height(100.dp)
                .clip(CircleShape) // This clips the image to a circle shape
                .border(3.dp, kilidPrimaryColor, CircleShape) // This adds a border; adjust thickness (2.dp) and color (Color.Black) as needed
        )

        Spacer(modifier = Modifier.height(8.dp))

        //share
        Text(
            text = contactInformation?.departmentName?: "",
            textAlign = TextAlign.Center,
            style = KilidTypography.h3 ,
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth())

        Spacer(modifier = Modifier.height(spaceBetweenButtons))
        //share
        Text(
            text = ("مشاور شما : " + contactInformation?.fullName) ?: "",
            textAlign = TextAlign.Center,
            style = KilidTypography.h4 ,
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth())

        Spacer(modifier = Modifier.height(spaceBetweenButtons*2))
        val customTextStyle = KilidTypography.h3.copy(fontSize = 16.sp) // Your custom text style

        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(
                fontSize = customTextStyle.fontSize,
                fontWeight = customTextStyle.fontWeight,
                letterSpacing = customTextStyle.letterSpacing,
                fontFamily = customTextStyle.fontFamily,
                // Add other properties from customTextStyle as needed
            )) {
                append("لطفا هنگام تماس  به من بگید که این آگهی را با کد ")
            }

            withStyle(style = SpanStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = kilidPrimaryColor,
                letterSpacing = customTextStyle.letterSpacing,
                fontFamily = customTextStyle.fontFamily,
            )) {
                append(identifier.toString())
            }

            withStyle(style = SpanStyle(
                fontSize = customTextStyle.fontSize,
                fontWeight = customTextStyle.fontWeight,
                letterSpacing = customTextStyle.letterSpacing,
                fontFamily = customTextStyle.fontFamily,
            )) {
                append(" در ")
            }

            withStyle(style = SpanStyle(
                fontSize = 18.sp ,
                fontWeight = FontWeight.Bold,
                color = kilidPrimaryColor,
                letterSpacing = customTextStyle.letterSpacing,
                fontFamily = customTextStyle.fontFamily,
            )) {
                append("سامانه کیلید ")
            }

            withStyle(style = SpanStyle(
                fontSize = customTextStyle.fontSize,
                fontWeight = customTextStyle.fontWeight,
                letterSpacing = customTextStyle.letterSpacing,
                fontFamily = customTextStyle.fontFamily,
            )) {
                append(" یافتید.")
            }
        }

        Text(text = annotatedString , modifier = Modifier.padding(horizontal = 8.dp))

        Spacer(modifier = Modifier.height(spaceBetweenButtons*2))


        val phoneText : String = when (contactInformationUIState){
            is UIState.Error ->{
                "خطایی پیش آمده"
            }
            UIState.Loading -> {
                "در حال دریافت اطلاعات"
            }
            UIState.Success ->{
                "تماس تلفنی : ${contactInformation?.callNumber}"
            }
            else -> {
                ""
            }

        }

        MobinButton(title = phoneText , painter = rememberAsyncImagePainter(model = R.drawable.phone_enabled) , modifier  = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(width = 1.dp, color = newSecondaryColor, shape = RoundedCornerShape(8.dp)),
            onClick = {
                try {
                    coroutineScope.launch {
                        val intent = Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:${contactInformation?.callNumber}")
                        )
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)

                        modalBottomSheetState.hide()

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(context, "شماره تلفن کپی شد", Toast.LENGTH_SHORT).show()
                }

            })


        Spacer(modifier = Modifier.height(25.dp))

    }
}
