package com.swaptech.meet.presentation.navigation.nav_graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.swaptech.meet.di.presentation.viewmodel.ViewModelFactory
import com.swaptech.meet.presentation.navigation.Auth
import com.swaptech.meet.presentation.screen.auth.AuthUserViewModel
import com.swaptech.meet.presentation.screen.auth.signin.SignInScreen
import com.swaptech.meet.presentation.screen.auth.signup.SignUpScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory
) {
    navigation(
        route = Auth.route,
        startDestination = Auth.SignIn.route
    ) {
        composable(
            route = Auth.SignIn.route
        ) {
            SignInScreen(
                viewModel = viewModel(
                    modelClass = AuthUserViewModel::class.java,
                    factory = viewModelFactory
                ),
                navController = navController
            )
        }
        composable(
            route = Auth.SignUp.route
        ) {
            SignUpScreen(
                viewModel = viewModel(
                    modelClass = AuthUserViewModel::class.java,
                    factory = viewModelFactory
                ),
                navController = navController
            )
        }
    }
}
