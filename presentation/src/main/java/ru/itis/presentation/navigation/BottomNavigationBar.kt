package ru.itis.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hierarchy
import ru.itis.presentation.R
import ru.itis.presentation.navigation.graphs.BolusNavScreen
import ru.itis.presentation.navigation.graphs.RootNavGraph

@Composable
fun BottomNavigationBar(navController: NavController, isBottomBarVisible: MutableState<Boolean>) {
    val items = listOf(
        BottomNavigationItem.Actions,
        BottomNavigationItem.Statistics,
        BottomNavigationItem.Basal,
        BottomNavigationItem.Profile
    )

    AnimatedVisibility(
        visible = isBottomBarVisible.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f))
            )
            NavigationBar(
                modifier = Modifier
                    .height(64.dp),
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        modifier = Modifier.offset(
                            when (item.title) {
                                BottomNavigationItem.Basal.title -> 20.dp
                                BottomNavigationItem.Statistics.title -> (-20).dp
                                else -> 0.dp
                            }
                        ),
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.secondary,
                        ),
                        alwaysShowLabel = false,
                        selected = currentDestination?.hierarchy?.any { it.route == item.graph } == true,
                        onClick = {
                            if (currentDestination?.hierarchy?.any{it.route == item.graph} == true) return@NavigationBarItem
                            navController.navigate(item.graph) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SetBottomNavigationBar() {
    val navController = rememberNavController()
    val fabShape = RoundedCornerShape(50)
    val isBottomBarVisible = rememberSaveable { (mutableStateOf(true)) }

    // BottomBar content:

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                AnimatedVisibility(
                    visible = isBottomBarVisible.value,
                    enter = slideInVertically(initialOffsetY = { 270 }),
                    exit = slideOutVertically(targetOffsetY = { 270 }),
                ) {
                    BottomNavigationBar(navController = navController, isBottomBarVisible = isBottomBarVisible)
                }
            }
        },

        floatingActionButton = {
            AnimatedVisibility(
                visible = isBottomBarVisible.value,
                enter = slideInVertically(initialOffsetY = { 270 }),
                exit = slideOutVertically(targetOffsetY = { 270 }),
            ) {
                FloatingActionButton(
                    modifier = Modifier.offset(0.dp, 40.dp),
                    onClick = {
                        navController.navigate(BolusNavScreen.Bolus.route)
                    },
                    shape = fabShape,
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        "",
                        modifier = Modifier.size(26.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            Box(
                modifier = Modifier
                    .imePadding() // Добавляем отступ только для контента
            ) {
                RootNavGraph(navController = navController, isBottomBarVisible)
            }
        }
    )
}