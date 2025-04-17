package ru.itis.presentation.navigation.graphs.bottom_bar

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.itis.presentation.navigation.BottomNavigationItem
import ru.itis.presentation.screens.actions.ActionsScreen

fun NavGraphBuilder.actionsNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = BottomNavigationItem.Actions.graph,
        startDestination = ActionsNavScreen.Actions.route
    ) {
        composable(
            route = ActionsNavScreen.Actions.route,
        ) {
            isBottomBarVisible.value = true
            ActionsScreen(navController)
        }
    }

}

sealed class ActionsNavScreen(val route: String) {

    object Actions : ActionsNavScreen(route = "actions")

    companion object {
        const val USER_ID_KEY = "userId"
        const val SELF_PROFILE = "self"
    }
}