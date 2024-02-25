package org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yamin8000.ppn.Digits
import org.lotka.xenonx.R
import org.lotka.xenonx.domain.enums.FilterTypes
import org.lotka.xenonx.domain.enums.HomeUseTypes
import org.lotka.xenonx.domain.enums.LandUseTypes
import org.lotka.xenonx.domain.enums.ListingType
import org.lotka.xenonx.presentation.composables.ThousandSeparatorInputVisualTransformator
import org.lotka.xenonx.presentation.composables.etc.MultiToggleButton
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.White
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidDarkBorders
import org.lotka.xenonx.presentation.theme.kilidDarkObjects
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteBorders
import org.lotka.xenonx.presentation.theme.kilidWhiteObjects
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.presentation.theme.newGrey50
import org.lotka.xenonx.presentation.theme.newPrimaryTextColor
import org.lotka.xenonx.presentation.theme.newSecondaryColor
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalLayoutApi::class)
@ExperimentalMaterialApi
@Composable
fun ListingFilterBottomSheet(
    viewModel: PlpViewModel,
    halfScreenModalBottomSheet: ModalBottomSheetState,
    fullScreenModalBottomSheet: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    onApplyFilterClicked: () -> Unit,
    onDismissFilterClicked: () -> Unit,
    configuration: Configuration,
    isDarkTheme: Boolean
    ) {

    //used for force recomposition
    viewModel.filterStateVersion

    val selectedAreas = viewModel.filterManager.getSelectedLocations()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkTheme) kilidDarkBackgound  else kilidWhiteBackgound)
    ) {

        //fixed part
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Divider(
                    Modifier
                        .width(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .align(Alignment.Center),
                    color = newSecondaryColor,
                    thickness = 8.dp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Text(
                    text = stringResource(id = R.string.sorting_and_filters),
                    style = KilidTypography.h5.copy(if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = stringResource(id = R.string.filter_reset),
                    style = KilidTypography.h3.copy(color = if (isDarkTheme) kilidDarkTexts else kilidPrimaryColor),
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            viewModel.clearAllFilters()
                        }
                    })

            }

            Spacer(modifier = Modifier.height(15.dp))
            Divider(color =  if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,)

        }


        //list part
        LazyColumn(
            modifier = Modifier
                .background(if (isDarkTheme) kilidDarkBackgound  else kilidWhiteBackgound )
                .fillMaxWidth()
                .padding(horizontal = 20.dp)

        ) {

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.selected_areas).plus(" :"),
                        style = KilidTypography.h4,
                       color = if (isDarkTheme) kilidDarkTexts else kilidPrimaryColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow {
                        selectedAreas.toList().forEachIndexed { index, item ->

                            val backgroundTint =  if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound
                            val textColor = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 2.dp, horizontal = 4.dp)
                                    .border(
                                        BorderStroke(1.dp, Color.LightGray),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(backgroundTint)
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                                    .toggleable(
                                        value = false,
                                        enabled = true,
                                        onValueChange = {}
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = item.phraseToShow ?: "",
                                    color = textColor,
                                    modifier = Modifier.padding(2.dp),
                                    style = KilidTypography.h3.copy(color = newPrimaryTextColor)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.Close, // You can replace this with your desired close icon
                                    contentDescription = "Close",
                                    tint = if (isDarkTheme) kilidWhiteObjects else kilidDarkObjects,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            viewModel.toggleCitySelection(item)
                                        }
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                            }
                        }

                        if (selectedAreas.isEmpty()){
                            Text(
                                text = "    محدوده یا محله ای  فیلتر نشده است    ",
                                style = KilidTypography.h2,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        Color.LightGray,
                                        RoundedCornerShape(25.dp)
                                    )
                                    .padding(vertical = 2.dp , horizontal = 8.dp)
                            )
                        }

                        var isSelected = true
                        val selectedTint = kilidPrimaryColor
                        val unselectedTint = newGrey50

                        val backgroundTint = if (isSelected) selectedTint else unselectedTint
                        val textColor = if (isSelected) Color.White else Color.Unspecified
                        Row(
                            modifier = Modifier
                                .padding(vertical = 2.dp, horizontal = 4.dp)
                                .border(
                                    BorderStroke(1.dp, Color.LightGray),
                                    RoundedCornerShape(8.dp)
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .background(backgroundTint)
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .toggleable(
                                    value = true,
                                    enabled = true,
                                    onValueChange = { selected ->

                                    }
                                )
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.fullScreenActiveBottomSheet =
                                            PlpBottomSheetType.LOCATION_SEARCH
                                        fullScreenModalBottomSheet.show()
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                "اضافه کردن محله جدید",
                                color = textColor,
                                modifier = Modifier.padding(2.dp),
                                style = KilidTypography.h2.copy(color = newPrimaryTextColor)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.Add, // You can replace this with your desired close icon
                                contentDescription = "add",
                                tint = newGrey50,
                                modifier = Modifier
                                    .size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                        }


                    }

                }

                Divider(Modifier.padding(vertical = 12.dp),color =  if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,)

            }


            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.advertisement_type).plus(" :"),
                        style = KilidTypography.h3,
                        color = if (isDarkTheme) kilidDarkTexts else kilidPrimaryColor,
                        modifier = Modifier.weight(1f)
                    )

                    MultiToggleButton(
                        currentSelection = viewModel.filterManager.getCurrentListingTypeSelection()?.nameFa
                            ?: "خطا",
                        toggleStates = ListingType.values().toList().map { it.nameFa },
                        onToggleChange = {
                            viewModel.addOrToggleFilter(
                                FilterTypes.LISTING_TYPE,
                                ListingType.getByFaName(it) ?: ListingType.SALE_AND_PRE_SALE
                            )
                        },
                        isDarkMode= isDarkTheme
                    )

                }

                Divider(Modifier.padding(vertical = 12.dp),color =  if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,)

            }




            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.user_type).plus(" :"),
                        style = KilidTypography.h3,
                        color = if (isDarkTheme) kilidDarkTexts else kilidPrimaryColor,
                        modifier = Modifier.weight(1f)
                    )


                    MultiToggleButton(
                        currentSelection = viewModel.filterManager.getCurrentLandUseTypeSelection().nameFa,
                        toggleStates = LandUseTypes.values().toList().map { it.nameFa },
                        onToggleChange = {
                            viewModel.addOrToggleFilter(
                                FilterTypes.LAND_USE_TYPE,
                                LandUseTypes.getLandUseTypeByNameFa(it) ?: LandUseTypes.ALL
                            )
                        },
                        isDarkMode= isDarkTheme
                    )

                }

                Divider(Modifier.padding(vertical = 12.dp),color =  if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,)
            }




            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.home_type).plus(" :"),
                        style = KilidTypography.h3,
                        color = if (isDarkTheme) kilidDarkTexts else kilidPrimaryColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val lsttp = when (viewModel.filterManager.getCurrentLandUseTypeSelection()) {
                        LandUseTypes.RESIDENTIAL -> HomeUseTypes.getResidentialHomeUseTypes()
                        LandUseTypes.COMMERCIAL -> HomeUseTypes.getCommercialHomeUseTypes()
                        else -> HomeUseTypes.getAllHomeUseTypes()
                    }
                    MultiToggleButton(
                        currentSelection = viewModel.filterManager.getCurrentHomeUseTypeSelection().nameFa,
                        toggleStates = lsttp.toList().map { it.nameFa },
                        modifier = Modifier.fillMaxWidth(),
                        onToggleChange = {
                            viewModel.addOrToggleFilter(
                                FilterTypes.HOME_USE_TYPE,
                                HomeUseTypes.getHomeUseTypeByNameFa(it) ?: LandUseTypes.ALL
                            )
                        },
                        isDarkMode= isDarkTheme
                        )


                }


                Divider(Modifier.padding(vertical = 12.dp),color =  if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,)

            }


            if (viewModel.filterManager.getCurrentListingTypeSelection()==ListingType.SALE_AND_PRE_SALE){
                //sale presale
                item {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.range_price),
                            style = KilidTypography.h3,
                            color = if (isDarkTheme) kilidDarkTexts else kilidPrimaryColor,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val minPriceText = if (viewModel.filterManager.totalPriceSemanticRange.min==null) "" else viewModel.filterManager.totalPriceSemanticRange.min.toString()
                        val maxPriceText = if (viewModel.filterManager.totalPriceSemanticRange.max==null) "" else viewModel.filterManager.totalPriceSemanticRange.max.toString()


                        if (minPriceText.isNotBlank() && maxPriceText.isNotBlank()){
                            if (minPriceText.toLong()>maxPriceText.toLong()){
                                Text(
                                    text = "میزان حداقل مبلغ  باید از حداکثر کمتر باشد",
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =   Color.Red,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                )
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth() , verticalAlignment = Alignment.Top  , horizontalArrangement = Arrangement.SpaceBetween) {



                            Column(modifier = Modifier.width((configuration.screenWidthDp/2) .dp -30.dp)) {



                                OutlinedTextField(
                                    placeholder  = {
                                        Text(
                                            text = "حداقل قیمت",
                                            style = KilidTypography.h3.copy(fontSize = 11.sp),
                                            color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        )
                                    },
                                    value = minPriceText,
                                    singleLine = true,
                                    onValueChange = { newValue ->
                                        viewModel.filterManager.setTotalPriceMin(newValue)
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.None,
                                        autoCorrect = true,
                                        keyboardType = KeyboardType.NumberPassword,
                                        imeAction = ImeAction.Next
                                    ),
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        focusedLabelColor = kilidPrimaryColor,
                                        backgroundColor = if (isDarkTheme) kilidDarkBackgound else White,
                                        cursorColor = kilidPrimaryColor,
                                        focusedIndicatorColor = kilidPrimaryColor,
                                        unfocusedIndicatorColor = if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,
                                    ),
                                    textStyle = KilidTypography.h3.copy(color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts),
                                    visualTransformation = ThousandSeparatorInputVisualTransformator(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = if (minPriceText.isBlank())
                                        stringResource(id = R.string.not_specified) else Digits().spellToFarsi(viewModel.filterManager.totalPriceSemanticRange.min.toString())
                                        .plus(" تومان"),
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                )
                            }

                            Column(modifier = Modifier.width((configuration.screenWidthDp/2) .dp -30.dp)) {




                                OutlinedTextField(
                                    placeholder  = {
                                        Text(
                                            text = "حداکثر قیمت",
                                            style = KilidTypography.h3.copy(fontSize = 11.sp),
                                            color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        )
                                    },
                                    value = maxPriceText,
                                    singleLine = true,
                                    onValueChange = { newValue ->
                                        viewModel.filterManager.setTotalPriceMax(newValue)
                                    }, keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.None,
                                        autoCorrect = true,
                                        keyboardType = KeyboardType.NumberPassword,
                                        imeAction = ImeAction.Next
                                    ), colors = TextFieldDefaults.textFieldColors(
                                        textColor = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        focusedLabelColor = kilidPrimaryColor,
                                        backgroundColor = if (isDarkTheme) kilidDarkBackgound else White,
                                        cursorColor = kilidPrimaryColor,
                                        focusedIndicatorColor = kilidPrimaryColor,
                                        unfocusedIndicatorColor = if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,
                                    ),
                                    textStyle = KilidTypography.h3.copy( color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts),
                                    visualTransformation = ThousandSeparatorInputVisualTransformator(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = if (maxPriceText.isBlank())
                                        stringResource(id = R.string.not_specified) else Digits().spellToFarsi(viewModel.filterManager.totalPriceSemanticRange.max.toString())
                                        .plus(" تومان"),
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                )



                            }
                        }


                    }


                    Divider(Modifier.padding(vertical = 8.dp),color =  if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,)

                }

            }else{
                //min  max rent
                item {
                    val minRentText = if (viewModel.filterManager.rentSemanticRange.min==null) "" else viewModel.filterManager.rentSemanticRange.min.toString()
                    val maxRentText = if (viewModel.filterManager.rentSemanticRange.max==null) "" else viewModel.filterManager.rentSemanticRange.max.toString()
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.range_rent),
                            color = if (isDarkTheme) kilidDarkTexts else kilidPrimaryColor,
                            style = KilidTypography.h3,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))


                        if (minRentText.isNotBlank() && maxRentText.isNotBlank()){
                            if (minRentText.toLong()>maxRentText.toLong()){
                                Text(
                                    text = "میزان حداقل اجاره  باید از حداکثر کمتر باشد",
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =   Color.Red,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                )
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth() , verticalAlignment = Alignment.Top  , horizontalArrangement = Arrangement.SpaceBetween) {

                            Column(modifier = Modifier.width((configuration.screenWidthDp/2) .dp -30.dp)) {

                                OutlinedTextField(
                                    placeholder  = {
                                        Text(
                                            text = "حداقل اجاره",
                                            style = KilidTypography.h3.copy(fontSize = 11.sp)

                                            , color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        )
                                    },
                                    value =minRentText,
                                    singleLine = true,
                                    onValueChange = { newValue ->
                                        viewModel.filterManager.setRentMin(newValue)
                                    }, keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.None,
                                        autoCorrect = true,
                                        keyboardType = KeyboardType.NumberPassword,
                                        imeAction = ImeAction.Next
                                    ), colors = TextFieldDefaults.textFieldColors(
                                        textColor = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        focusedLabelColor = kilidPrimaryColor,
                                        backgroundColor = if (isDarkTheme) kilidDarkBackgound else White,
                                        cursorColor = kilidPrimaryColor,
                                        focusedIndicatorColor = kilidPrimaryColor,
                                        unfocusedIndicatorColor = if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,
                                    ),
                                    textStyle = KilidTypography.h3.copy(color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts),
                                    visualTransformation = ThousandSeparatorInputVisualTransformator(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = if (minRentText.isBlank())
                                        stringResource(id = R.string.not_specified) else Digits().spellToFarsi(viewModel.filterManager.rentSemanticRange.min.toString())
                                        .plus(" تومان"),
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                )
                            }

                            Column(modifier = Modifier.width((configuration.screenWidthDp/2) .dp -30.dp)) {


                                OutlinedTextField(
                                    placeholder  = {
                                        Text(
                                            text = "حداکثر اجاره",
                                            style = KilidTypography.h3.copy(fontSize = 11.sp),
                                            color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        )
                                    },
                                    value =maxRentText,
                                    singleLine = true,
                                    onValueChange = { newValue ->
                                        viewModel.filterManager.setRentMax(newValue)

                                    }, keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.None,
                                        autoCorrect = true,
                                        keyboardType = KeyboardType.NumberPassword,
                                        imeAction = ImeAction.Next
                                    ), colors = TextFieldDefaults.textFieldColors(
                                        textColor = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        focusedLabelColor = kilidPrimaryColor,
                                        backgroundColor = if (isDarkTheme) kilidDarkBackgound else White,
                                        cursorColor = kilidPrimaryColor,
                                        focusedIndicatorColor = kilidPrimaryColor,
                                        unfocusedIndicatorColor = if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,
                                    ),
                                    textStyle = KilidTypography.h3.copy(color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts),
                                    visualTransformation = ThousandSeparatorInputVisualTransformator(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = if (maxRentText.isBlank())
                                        stringResource(id = R.string.not_specified) else Digits().spellToFarsi(viewModel.filterManager.rentSemanticRange.max.toString())
                                        .plus(" تومان"),
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                )


                            }
                        }


                    }


                    Divider(Modifier.padding(vertical = 8.dp),color =  if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,)

                }

                //min  max deposit
                item {
                    val minDepositText = if (viewModel.filterManager.depositSemanticRange.min==null) "" else viewModel.filterManager.depositSemanticRange.min.toString()
                    val maxDepositText = if (viewModel.filterManager.depositSemanticRange.max==null) "" else viewModel.filterManager.depositSemanticRange.max.toString()
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.range_deposit),
                            color = if (isDarkTheme) kilidDarkTexts else kilidPrimaryColor,
                            style = KilidTypography.h3,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (minDepositText.isNotBlank() && maxDepositText.isNotBlank()){
                            if (minDepositText.toLong()>maxDepositText.toLong()){
                                Text(
                                    text = "میزان رهن   باید از حداکثر کمتر باشد",
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =   Color.Red,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                )
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth() , verticalAlignment = Alignment.Top  , horizontalArrangement = Arrangement.SpaceBetween) {

                            Column(modifier = Modifier.width((configuration.screenWidthDp/2) .dp -30.dp)) {

                                OutlinedTextField(
                                    placeholder  = {
                                        Text(
                                            text = "حداقل رهن",
                                            style = KilidTypography.h3.copy(fontSize = 11.sp),
                                            color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        )
                                    },
                                    value = minDepositText,
                                    singleLine = true,
                                    onValueChange = { newValue ->
                                        viewModel.filterManager.setDepositMin(newValue)
                                    }, keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.None,
                                        autoCorrect = true,
                                        keyboardType = KeyboardType.NumberPassword,
                                        imeAction = ImeAction.Next
                                    ), colors = TextFieldDefaults.textFieldColors(
                                        textColor = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        focusedLabelColor = kilidPrimaryColor,
                                        backgroundColor = if (isDarkTheme) kilidDarkBackgound else White,
                                        cursorColor = kilidPrimaryColor,
                                        focusedIndicatorColor = kilidPrimaryColor,
                                        unfocusedIndicatorColor = if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,
                                    ),
                                    textStyle = KilidTypography.h3.copy(color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts),
                                    visualTransformation = ThousandSeparatorInputVisualTransformator(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = if (minDepositText.isBlank())
                                        stringResource(id = R.string.not_specified) else Digits().spellToFarsi(viewModel.filterManager.depositSemanticRange.min.toString())
                                        .plus(" تومان"),
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                )
                            }

                            Column(modifier = Modifier.width((configuration.screenWidthDp/2) .dp -30.dp)) {


                                OutlinedTextField(
                                    placeholder  = {
                                        Text(
                                            text = "حداکثر رهن",
                                            style = KilidTypography.h3.copy(fontSize = 11.sp),
                                            color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        )
                                    },
                                    value = maxDepositText,
                                    singleLine = true,
                                    onValueChange = { newValue ->
                                        viewModel.filterManager.setDepositMax(newValue)
                                    }, keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.None,
                                        autoCorrect = true,
                                        keyboardType = KeyboardType.NumberPassword,
                                        imeAction = ImeAction.Next
                                    ), colors = TextFieldDefaults.textFieldColors(
                                        textColor = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                        focusedLabelColor = kilidPrimaryColor,
                                        backgroundColor = if (isDarkTheme) kilidDarkBackgound else White,
                                        cursorColor = kilidPrimaryColor,
                                        focusedIndicatorColor = kilidPrimaryColor,
                                        unfocusedIndicatorColor = if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,
                                    ),
                                    textStyle = KilidTypography.h3.copy(color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts),
                                    visualTransformation = ThousandSeparatorInputVisualTransformator(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = if (maxDepositText.isBlank())
                                        stringResource(id = R.string.not_specified) else Digits().spellToFarsi(viewModel.filterManager.depositSemanticRange.max.toString())
                                        .plus(" تومان"),
                                    style = KilidTypography.h3.copy(fontSize = 11.sp),
                                    color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                                )
                            }
                        }


                    }


                        //Divider(Modifier.padding(vertical = 8.dp) , color =  if (isDarkTheme) kilidDarkBorders else kilidWhiteBorders,)

                }
            }





            item {
                Spacer(modifier = Modifier.height(configuration.screenHeightDp.dp/2))
            }

        }


    }


}
