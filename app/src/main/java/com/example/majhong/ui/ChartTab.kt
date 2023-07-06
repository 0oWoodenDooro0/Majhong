package com.example.majhong.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.majhong.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChartTab(pageState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(selectedTabIndex = pageState.currentPage) {
        TabItem(
            selectedTab = pageState.currentPage == 0,
            onClick = {
                coroutineScope.launch {
                    pageState.animateScrollToPage(0)
                }
            },
            text = "統計數字",
            icon = if (pageState.currentPage == 0) painterResource(id = R.drawable.baseline_emoji_events_24)
            else painterResource(id = R.drawable.outline_emoji_events_24),
            content = stringResource(id = R.string.chart_content),
            color = if (pageState.currentPage == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        TabItem(
            selectedTab = pageState.currentPage == 1,
            onClick = {
                coroutineScope.launch {
                    pageState.animateScrollToPage(1)
                }
            },
            text = "歷史紀錄",
            icon = painterResource(id = R.drawable.baseline_format_list_bulleted_24),
            content = stringResource(id = R.string.list_content),
            color = if (pageState.currentPage == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}