package ru.itis.presentation.navigation.graphs.bottom_bar

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.itis.presentation.navigation.BottomNavigationItem
import ru.itis.presentation.screens.basal.BasalScreen

fun NavGraphBuilder.basalNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Basal.graph,
        startDestination = BasalNavScreen.Basal.route
    ) {
        composable(
            route = BasalNavScreen.Basal.route,
        ) {
            isBottomBarVisible.value = true
            BasalScreen(navController)
        }
    }

}

sealed class BasalNavScreen(val route: String) {

    object Basal : BasalNavScreen(route = "basal")

    companion object {
        const val USER_ID_KEY = "userId"
        const val SELF_PROFILE = "self"
    }
}