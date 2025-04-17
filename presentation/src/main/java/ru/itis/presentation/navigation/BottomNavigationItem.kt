package ru.itis.presentation.navigation

import ru.itis.presentation.R

sealed class BottomNavigationItem(var graph: String, var icon: Int, var title: String) {

    object Actions : BottomNavigationItem(
        "actions_graph",
        R.drawable.ic_actions,
        "Actions"
    )

    object Statistics : BottomNavigationItem(
        "statistics_graph",
        R.drawable.ic_statistics,
        "Statistics"
    )

    object Basal : BottomNavigationItem(
        "basal_graph",
        R.drawable.ic_basal,
        "Basal"
    )

    object Profile : BottomNavigationItem(
        "profile_graph",
        R.drawable.ic_profile,
        "Profile"
    )
}