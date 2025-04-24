package ru.itis.presentation.navigation.graphs

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import ru.itis.presentation.navigation.BottomNavigationItem
import ru.itis.presentation.navigation.graphs.bottom_bar.actionsNavGraph
import ru.itis.presentation.navigation.graphs.bottom_bar.basalNavGraph
import ru.itis.presentation.navigation.graphs.bottom_bar.profileNavGraph
import ru.itis.presentation.navigation.graphs.bottom_bar.statisticsNavGraph

@Composable
fun RootNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION,
        modifier = Modifier
            .systemBarsPadding()
            .navigationBarsPadding()
    ) {
        authNavGraph(navController = navController, isBottomBarVisible)
        bottomNavGraph(navController = navController, isBottomBarVisible)
    }
}



fun NavGraphBuilder.bottomNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        startDestination = BottomNavigationItem.Profile.graph,
        route = Graph.BOTTOM
    ) {
        //Графы к каждой вкладке Bottom Navigation:
        actionsNavGraph(navController = navController, isBottomBarVisible)
        statisticsNavGraph(navController = navController, isBottomBarVisible)
        basalNavGraph(navController = navController, isBottomBarVisible)
        profileNavGraph(navController = navController, isBottomBarVisible)

        bolusNavGraph(navController = navController, isBottomBarVisible)
    }
}

object Graph {
    const val BOLUS = "bolus_graph"
    const val AUTHENTICATION = "auth_graph"
    const val BOTTOM = "bottom_graph"
    const val ROOT = "root_graph"
}