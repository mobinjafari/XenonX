package org.lotka.xenonx.presentation.ui.screens.plp.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lotka.xenonx.R
import org.lotka.xenonx.domain.enums.FilterTypes
import org.lotka.xenonx.domain.enums.FilterTypes.Companion.getFilterTypeByServerKey
import org.lotka.xenonx.domain.enums.HomeUseTypes
import org.lotka.xenonx.domain.enums.LandUseTypes
import org.lotka.xenonx.domain.enums.ListingType
import org.lotka.xenonx.domain.model.model.GeneralPurposeSemanticFilterV2RangeDTO
import org.lotka.xenonx.presentation.composables.etc.MobinButton
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.White
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
import org.lotka.xenonx.util.extension.toReadableCurrency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlpFilterPart(
    isDarkTheme : Boolean,
    viewModel: PlpViewModel,
    coroutineScope : CoroutineScope,
    configuration : Configuration,
    onFilterClick: () -> Unit,
    onLocationSelectClick: () -> Unit ) {

    //used for force recomposition
    viewModel.filterStateVersion

    val job = remember { mutableStateOf<Job?>(null) }
    Column(modifier = Modifier.background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)) {

        Row(
            Modifier
              //  .height(configuration.screenHeightDp.dp/11)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            OutlinedTextField(
                placeholder = {
                    Text(
                        text = "جستجوی محله یا شهر",
                        color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                        style = KilidTypography.h2,
                        modifier = Modifier.fillMaxSize(),
                    )
                },
                value = "",
                onValueChange = {},
                enabled = false,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts,
                    focusedLabelColor = kilidPrimaryColor,
                    backgroundColor = if (isDarkTheme) kilidDarkBackgound else White,
                    cursorColor = kilidPrimaryColor
                ),
                textStyle = KilidTypography.h2.copy(color =  if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clickable{
                        onLocationSelectClick()
                        viewModel.onHalfScreenBottomSheetOpened()
                    }
            )
            Spacer(modifier = Modifier.width(8.dp))
            //load from drawable
            MobinButton(
                title = "فیلتر ها",
                painter = painterResource(id = R.drawable.ic_filter),
                imageTint = Color.White,
                onClick = { onFilterClick() },
                modifier = Modifier.height(56.dp)
            )

        }

//        Text(
//            text = "page : ${viewModel.page.value} ",
//            style = KilidTypography.h3.copy(color =   if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts , fontSize = 14.sp),
//            modifier = Modifier.fillMaxWidth(),
//          textAlign = TextAlign.End
//        )
//        Text(
//            text = "latestindex : ${viewModel.latestindex.value} ",
//            style = KilidTypography.h3.copy(color =   if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts , fontSize = 14.sp),
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.End
//        )
//        Text(
//            text = "state : ${viewModel.sessionUiState.collectAsState().value} ",
//            style = KilidTypography.h3.copy(color =   if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts , fontSize = 14.sp),
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.End
//        )
//
//        Text(
//            text = "total : ${viewModel.sessions.collectAsState().value.size} ",
//            style = KilidTypography.h3.copy(color =   if (isDarkTheme) kilidDarkTexts else kilidWhiteTexts , fontSize = 14.sp),
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.End
//        )

        if(viewModel.filterManager.getActiveFilters().isNotEmpty()){
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                item {

                }
                items(viewModel.filterManager.getActiveFilters().toList()) { filter ->
                    FilterChip(
                        onClick = { },
                        selected = true,
                        colors = ChipDefaults.filterChipColors(
                            backgroundColor = if (isDarkTheme) kilidDarkBackgound else kilidPrimaryColor,
                            disabledBackgroundColor = org.lotka.xenonx.presentation.theme.StoneGray,
                            selectedBackgroundColor = org.lotka.xenonx.presentation.theme.kilidPrimaryColor,
                        ),
                        modifier = Modifier.padding(horizontal = 2.dp),
                    ) {
                        val filterType : FilterTypes? = getFilterTypeByServerKey(filter.first)
                        val filterName = filterType?.nameFa ?: ""
                        val filterValue = when (filterType) {
                            FilterTypes.LISTING_TYPE -> {
                                ListingType.getValueById(filter.second as Int)?.nameFa
                            }
                            FilterTypes.LAND_USE_TYPE -> {
                                LandUseTypes.getLandUseTypeByID(filter.second as Array<Int>)?.nameFa ?: ""
                            }
                            FilterTypes.HOME_USE_TYPE ->{
                                HomeUseTypes.getHomeUseTypeById(filter.second as Array<Int>)?.nameFa ?: ""
                            }
                            FilterTypes.LOCATION -> {
                                (filter.second as List<String>).size.toString().plus(" مورد ")
                            }
                            FilterTypes.SALE_PRICE_RANGE -> {
                                var item =  (filter.second as GeneralPurposeSemanticFilterV2RangeDTO)
                                var min = item.min?.toString()?.toReadableCurrency()?:null
                                var max = item.max?.toString()?.toReadableCurrency()?:null
                                var str = StringBuilder()
                                if (min!=null){
                                    str.append("از").append(" ").append(min)
                                }
                                if(max!=null){
                                    str.append("تا").append(" ").append(max)
                                }
                                str.toString()
                            }
                            FilterTypes.DEPOSIT_PRICE_RANGE -> {
                                var item =  (filter.second as GeneralPurposeSemanticFilterV2RangeDTO)
                                var min = item.min?.toString()?.toReadableCurrency()?:null
                                var max = item.max?.toString()?.toReadableCurrency()?:null
                                var str = StringBuilder()
                                if (min!=null){
                                    str.append("از").append(" ").append(min)
                                }
                                if(max!=null){
                                    str.append("تا").append(" ").append(max)
                                }
                                str.toString()
                            }
                            FilterTypes.RENT_PRICE_RANGE -> {
                                var item =  (filter.second as GeneralPurposeSemanticFilterV2RangeDTO)
                                var min = item.min?.toString()?.toReadableCurrency()?:null
                                var max = item.max?.toString()?.toReadableCurrency()?:null
                                var str = StringBuilder()
                                if (min!=null){
                                    str.append("از").append(" ").append(min)
                                }
                                if(max!=null){
                                    str.append("تا").append(" ").append(max)
                                }
                                str.toString()
                            }
                            else -> {
                                ""
                            }
                        }
                        Text(text = "$filterName : $filterValue", style = KilidTypography.h3.copy(color = Color.White, fontSize = 12.sp))

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Default.Close, // You can replace this with your desired close icon
                            contentDescription = "Close",
                            tint = White ,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable {
                                    if (filterType != null) {
                                        viewModel.removeFilter(filterType)
                                    }
                                }
                        )
                    }
                }
            }
        }


    }




}