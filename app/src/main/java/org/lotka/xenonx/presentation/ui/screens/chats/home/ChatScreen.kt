package org.lotka.xenonx.presentation.ui.screens.chats.home



import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.lotka.xenonx.presentation.theme.KilidTypography


@SuppressLint("AutoboxingStateCreation")
@Composable
fun HomeTabRow(
    navController: NavController
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        "Settings",
        "Search",
        "Chats"
    )

    val tabContent: @Composable (Int) -> Unit = { tabIndex ->
        when (tabIndex) {
            0 -> ChatsTabRow()
            1 -> SearchTabRow( )
            2 -> SettingsTabRow( )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = Color.Black,
            backgroundColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),style = KilidTypography.h3
                        , fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        tabContent(selectedTabIndex)



    }
}

@Composable
fun SettingsTabRow() {

}

@Composable
fun SearchTabRow() {

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatsTabRow( ) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {




        }
        }}}






