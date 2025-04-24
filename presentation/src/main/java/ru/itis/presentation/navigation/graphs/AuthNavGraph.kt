package ru.itis.presentation.navigation.graphs

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.itis.presentation.screens.auth.fill_health.FillHealthScreen
import ru.itis.presentation.screens.auth.fill_profile.FillProfileScreen
import ru.itis.presentation.screens.auth.greeting.GreetingScreen
import ru.itis.presentation.screens.auth.sign_in.SignInScreen
import ru.itis.presentation.screens.auth.sign_up.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController, isBottomBarVisible: MutableState<Boolean>) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthNavScreen.SignIn.route
    ) {
        composable(route = AuthNavScreen.SignIn.route) {
            isBottomBarVisible.value = false
            SignInScreen(navController)
        }
        composable(route = AuthNavScreen.SignUp.route) {
            isBottomBarVisible.value = false
            SignUpScreen(navController)
        }
        composable(route = AuthNavScreen.Greeting.route) {
            isBottomBarVisible.value = false
            GreetingScreen(navController)
        }
        composable(route = AuthNavScreen.FillProfile.route) {
            isBottomBarVisible.value = false
            FillProfileScreen(navController)
        }
        composable(route = AuthNavScreen.FillHealth.route) {
            isBottomBarVisible.value = false
            FillHealthScreen(navController)
        }
        composable(route = AuthNavScreen.Forgot.route) {
            //TODO
        }
    }
}

sealed class AuthNavScreen(val route: String) {
    object SignIn : AuthNavScreen(route = "sign_in")
    object SignUp : AuthNavScreen(route = "sign_up")
    object Greeting : AuthNavScreen(route = "greeting")
    object FillProfile : AuthNavScreen(route = "fill_profile")
    object FillHealth : AuthNavScreen(route = "fill_health")
    object Forgot : AuthNavScreen(route = "forgot") //TODO
}