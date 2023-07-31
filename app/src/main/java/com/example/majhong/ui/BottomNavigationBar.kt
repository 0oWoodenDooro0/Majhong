package com.example.majhong.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomNavigationBar(pageState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val items = listOf(NavigationBarItem.Game, NavigationBarItem.Chart, NavigationBarItem.History)
    NavigationBar {
        items.forEachIndexed { index, navigationBarItem ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = if (pageState.currentPage == index) navigationBarItem.selectedIcon else navigationBarItem.unselectedIcon),
                        contentDescription = stringResource(id = navigationBarItem.content),
                        tint = if (pageState.currentPage == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = { Text(text = navigationBarItem.title) },
                selected = pageState.currentPage == index,
                onClick = {
                    coroutineScope.launch { pageState.animateScrollToPage(index) }
                }
            )
        }
    }
}
