package com.example.majhong.ui

import com.example.majhong.R

sealed class NavigationBarItem(
    var title: String,
    var selectedIcon: Int,
    var unselectedIcon: Int,
    var content: Int
) {
    object Game : NavigationBarItem(
        "目前賽況",
        R.drawable.baseline_sports_esports_24,
        R.drawable.outline_sports_esports_24,
        R.string.sport_content
    )

    object Chart : NavigationBarItem(
        "統計數字",
        R.drawable.baseline_emoji_events_24,
        R.drawable.outline_emoji_events_24,
        R.string.chart_content
    )

    object History : NavigationBarItem(
        "歷史紀錄",
        R.drawable.baseline_format_list_bulleted_24,
        R.drawable.baseline_format_list_bulleted_24,
        R.string.list_content
    )
}
