package ru.itis.presentation.navigation.graphs

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.itis.presentation.screens.bolus.bolus.BolusScreen
import ru.itis.presentation.screens.bolus.calculate_bolus.CalculateBolusScreen
import ru.itis.presentation.screens.bolus.edit_calculate_bolus.EditCalculateBolusScreen
import ru.itis.presentation.screens.bolus.insulin_injection.BolusInjectionScreen


fun NavGraphBuilder.bolusNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = Graph.BOLUS,
        startDestination = BolusNavScreen.Bolus.route
    ) {
        composable(route = BolusNavScreen.Bolus.route) {
            isBottomBarVisible.value = false
            BolusScreen(navController)
        }
        composable(
            route = BolusNavScreen.CalculateBolus.route,
            arguments = listOf(
                navArgument("nutritionValue") {
                    type = NavType.FloatType
                    defaultValue = 0f
                },
                navArgument("glucoseValue") {
                    type = NavType.FloatType
                    defaultValue = 0f
                }
            )
        ) {
            isBottomBarVisible.value = false
            CalculateBolusScreen(navController)
        }
        composable(
            route = BolusNavScreen.EditCalculateBolus.route,
            arguments = listOf(
                navArgument("bolusValue") {
                    type = NavType.FloatType
                    defaultValue = 0f
                }
            )
        ) {
            isBottomBarVisible.value = false
            EditCalculateBolusScreen(navController)
        }
        composable(
            route = BolusNavScreen.BolusInjection.route,
            arguments = listOf(
                navArgument("bolusValue") {
                    type = NavType.FloatType
                    defaultValue = 0f
                }
            )
        ) {
            isBottomBarVisible.value = false
            BolusInjectionScreen(navController)
        }
    }
}

sealed class BolusNavScreen(val route: String) {
    object Bolus : BolusNavScreen(route = "bolus")
    object CalculateBolus : BolusNavScreen(
        route = "calculate_bolus?nutritionValue={nutritionValue}&glucoseValue={glucoseValue}"
    ) {
        fun createRoute(
            nutritionValue: Float,
            glucoseValue: Float
        ) = "calculate_bolus?nutritionValue=$nutritionValue&glucoseValue=$glucoseValue"
    }
    object EditCalculateBolus : BolusNavScreen(
        route = "edit_calculate_bolus?bolusValue={bolusValue}"
    ){
        fun createRoute(
            bolusValue: Float
        ) = "edit_calculate_bolus?bolusValue=$bolusValue"
    }
    object BolusInjection : BolusNavScreen(
        route = "bolus_injection?bolusValue={bolusValue}"
    ){
        fun createRoute(
            bolusValue: Float
        ) = "bolus_injection?bolusValue=$bolusValue"
    }
}