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
        startDestination = AuthScreen.SignIn.route
    ) {
        composable(route = AuthScreen.SignIn.route) {
            isBottomBarVisible.value = false
            SignInScreen(navController)
        }
        composable(route = AuthScreen.SignUp.route) {
            isBottomBarVisible.value = false
            SignUpScreen(navController)
        }
        composable(route = AuthScreen.Greeting.route) {
            isBottomBarVisible.value = false
            GreetingScreen(navController)
        }
        composable(route = AuthScreen.FillProfile.route) {
            isBottomBarVisible.value = false
            FillProfileScreen(navController)
        }
        composable(route = AuthScreen.FillHealth.route) {
            isBottomBarVisible.value = false
            FillHealthScreen(navController)
        }
        composable(route = AuthScreen.Forgot.route) {
            //TODO
        }
    }
}

sealed class AuthScreen(val route: String) {
    object SignIn : AuthScreen(route = "sign_in")
    object SignUp : AuthScreen(route = "sign_up")
    object Greeting : AuthScreen(route = "greeting")
    object FillProfile : AuthScreen(route = "fill_profile")
    object FillHealth : AuthScreen(route = "fill_health")
    object Forgot : AuthScreen(route = "forgot") //TODO
}