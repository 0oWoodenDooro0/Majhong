package com.example.majhong.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.majhong.database.Player

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChartScreen(playerList: () -> List<Player>) {
    val pageState = rememberPagerState(initialPage = 0)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ChartTab(pageState) }
    ) { padding ->
        HorizontalPager(pageCount = 2, state = pageState) { page ->
            Column(modifier = Modifier.padding(padding)) {
                if (page == 0) {
                    CountScreen(playerList = playerList)
                } else if (page == 1) {

                }
            }
        }
    }
}