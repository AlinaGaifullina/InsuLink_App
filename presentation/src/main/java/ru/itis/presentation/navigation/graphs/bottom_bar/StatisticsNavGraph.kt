package ru.itis.presentation.navigation.graphs.bottom_bar

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.itis.presentation.navigation.BottomNavigationItem
import ru.itis.presentation.screens.statistics.StatisticsScreen

fun NavGraphBuilder.statisticsNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Statistics.graph,
        startDestination = StatisticsNavScreen.Statistics.route
    ) {
        composable(
            route = StatisticsNavScreen.Statistics.route,
        ) {
            isBottomBarVisible.value = true
            StatisticsScreen(navController)
        }
    }

}

sealed class StatisticsNavScreen(val route: String) {

    object Statistics : StatisticsNavScreen(route = "statistics")

    companion object {
        const val USER_ID_KEY = "userId"
        const val SELF_PROFILE = "self"
    }
}