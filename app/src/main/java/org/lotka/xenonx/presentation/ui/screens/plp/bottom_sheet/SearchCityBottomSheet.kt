package org.lotka.xenonx.presentation.ui.screens.plp.bottom_sheet

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ChipDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.lotka.xenonx.R
import org.lotka.xenonx.domain.model.model.location.LocationSearchItem
import org.lotka.xenonx.presentation.composables.etc.general.LottieLoading
import org.lotka.xenonx.presentation.composables.reusables.GeneralSearchBar
import org.lotka.xenonx.presentation.theme.KilidTypography
import org.lotka.xenonx.presentation.theme.StoneGray
import org.lotka.xenonx.presentation.theme.White
import org.lotka.xenonx.presentation.theme.kilidDarkBackgound
import org.lotka.xenonx.presentation.theme.kilidDarkObjects
import org.lotka.xenonx.presentation.theme.kilidDarkTexts
import org.lotka.xenonx.presentation.theme.kilidPrimaryColor
import org.lotka.xenonx.presentation.theme.kilidWhiteBackgound
import org.lotka.xenonx.presentation.theme.kilidWhiteObjects
import org.lotka.xenonx.presentation.theme.kilidWhiteTexts
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
import org.lotka.xenonx.presentation.util.UIState
import kotlinx.coroutines.CoroutineScope
import java.util.Collections

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun SearchCityBottomSheet(
    viewModel: PlpViewModel,
    modalBottomSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    onApplyFilterClicked: () -> Unit,
    onDismissFilterClicked: () -> Unit,
    configuration: Configuration,
    isDarkTheme: Boolean,
    selectedAreas: List<LocationSearchItem>,
    onAddNewArea : (LocationSearchItem) -> Unit,
    onRemoveArea: (LocationSearchItem) -> Unit,

) {
    //used for force recomposition
    viewModel.filterStateVersion

    val searchQuery by viewModel.locationSearchQuery.collectAsState()

    val data by viewModel.searchAreaResult.collectAsState(initial = Collections.emptyList())
    val uiState by viewModel.searchAreaUiState.collectAsState(initial = UIState.Idle)

    // Create an instance of FocusRequester
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyListState: LazyListState = rememberLazyListState()

    // Remember the previous size of the list
    var previousSize by remember { mutableIntStateOf(selectedAreas.size) }

    LaunchedEffect(selectedAreas.size) {
        if (selectedAreas.size > previousSize) {
            lazyListState.animateScrollToItem(selectedAreas.size - 1)
        }
        previousSize = selectedAreas.size
    }
    LaunchedEffect(modalBottomSheetState) {
        if (modalBottomSheetState.currentValue == ModalBottomSheetValue.Expanded) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GeneralSearchBar(
                value = searchQuery,
                placeHolder = "جستجوی شهر یا محله",
                onValueChange = {
                    viewModel.setSearchQuery(it)
                },
                onValueRemoved = viewModel::clearSearchQuery,
                onKeyboardActions = viewModel::performSearch,
                onPerformSearch = viewModel::performSearch,
                isDarkTheme = isDarkTheme,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .focusRequester(focusRequester)
            )
            Icon(painter = rememberAsyncImagePainter(R.drawable.baseline_arrow_back_24),
                tint = if(isDarkTheme) kilidWhiteObjects else kilidDarkObjects,
                contentDescription = "agency",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp)
                    .clickable {
                        onDismissFilterClicked()
                    })
        }
        AnimatedVisibility(
            visible = uiState == UIState.Loading,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandVertically(
                animationSpec = tween(durationMillis = 1000)
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkVertically(
                animationSpec = tween(durationMillis = 1000)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottieLoading(size = 35.dp)
                Text(text = "در حال جستجوی \"$searchQuery\"", style = KilidTypography.h3 ,color = if (isDarkTheme)  kilidDarkTexts else  kilidWhiteTexts )
            }
        }
        when (uiState) {
            is UIState.Error -> {
                Text(text = "خطا در جستجو ${(uiState as UIState.Error).message.toString()}" ,color = if (isDarkTheme)  kilidDarkTexts else  kilidWhiteTexts )
            }
            else -> {}
        }
        AnimatedVisibility(
            visible = selectedAreas.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandVertically(
                animationSpec = tween(durationMillis = 1000)
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkVertically(
                animationSpec = tween(durationMillis = 1000)
            )
        ) {
            Column(modifier = Modifier.background(  if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)) {
                LazyRow(
                    state = lazyListState,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    items(selectedAreas) { filter ->
                        FilterChip(
                            onClick = {

                            },
                            selected = true,
                            colors = ChipDefaults.filterChipColors(
                                backgroundColor = StoneGray,
                                disabledBackgroundColor = StoneGray,
                                selectedBackgroundColor = kilidPrimaryColor,
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp),
                        ) {

                            Text(
                                text = "${filter.exactNameLocal} ",
                                style = KilidTypography.h3.copy(
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(imageVector = Icons.Default.Close, // You can replace this with your desired close icon
                                contentDescription = "Close",
                                tint =  White,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable {
                                        viewModel.toggleCitySelection(filter)
                                    })
                        }
                    }
                }
                Divider()
            }


        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background( if (isDarkTheme) kilidDarkBackgound else kilidWhiteBackgound)
                .padding(horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (data.isEmpty() && uiState !is UIState.Loading) {
                if (searchQuery.isNotEmpty()) {
                    item {
                        Text(
                            text = "موردی یافت نشد",
                            style = KilidTypography.h4,
                            color = if (isDarkTheme)  kilidDarkTexts else  kilidWhiteTexts ,
                            modifier = Modifier.padding(top = 100.dp)
                        )
                    }
                } else {
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            text = "لطفا شهر یا محله مورد نظر خود را جستجو کنید",
                            style = KilidTypography.h4,
                            color = if (isDarkTheme)  kilidDarkTexts else  kilidWhiteTexts ,
                            modifier = Modifier.padding(top = 100.dp)
                        )
                    }
                }

            } else {
                itemsIndexed(items = data) { index, recipe ->
                    if (recipe != null) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.toggleCitySelection(recipe)
                            }
                            .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = recipe.phraseToShow ?: "",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f),
                                color = if (isDarkTheme)  kilidDarkTexts else  kilidWhiteTexts ,
                                style = KilidTypography.h3
                            )
                            Checkbox(
                                checked = recipe.isSelected,
                                onCheckedChange = {
                                    viewModel.toggleCitySelection(recipe)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkmarkColor =White,
                                    checkedColor = kilidPrimaryColor,
                                    uncheckedColor = if (isDarkTheme) Color.White else Color.Black,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

