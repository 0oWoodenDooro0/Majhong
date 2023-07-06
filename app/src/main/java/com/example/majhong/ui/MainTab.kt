package com.example.majhong.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.majhong.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainTab(pageState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(selectedTabIndex = pageState.currentPage) {
        TabItem(
            selectedTab = pageState.currentPage == 0,
            onClick = {
                coroutineScope.launch {
                    pageState.animateScrollToPage(0)
                }
            },
            text = "目前賽況",
            icon = if (pageState.currentPage == 0) painterResource(id = R.drawable.baseline_sports_esports_24)
            else painterResource(id = R.drawable.outline_sports_esports_24),
            content = stringResource(id = R.string.sport_content),
            color = if (pageState.currentPage == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        TabItem(
            selectedTab = pageState.currentPage == 1,
            onClick = {
                coroutineScope.launch {
                    pageState.animateScrollToPage(1)
                }
            },
            text = "牌局統計",
            icon = painterResource(id = R.drawable.baseline_bar_chart_24),
            content = stringResource(id = R.string.chart_content),
            color = if (pageState.currentPage == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TabItem(
    selectedTab: Boolean,
    onClick: () -> Unit,
    text: String,
    icon: Painter,
    content: String,
    color: Color
) {
    Tab(selected = selectedTab,
        onClick = onClick,
        text = { Text(text = text, color = color) },
        icon = { Icon(painter = icon, contentDescription = content, tint = color) })
}