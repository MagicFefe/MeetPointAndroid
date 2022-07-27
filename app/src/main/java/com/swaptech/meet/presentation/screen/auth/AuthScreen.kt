package com.swaptech.meet.presentation.screen.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swaptech.meet.di.presentation.viewmodel.ViewModelFactory
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.screen.auth.signin.SignInScreen
import com.swaptech.meet.presentation.screen.auth.signup.SignUpScreen
import com.swaptech.meet.presentation.screen.auth.signup.SignUpViewModel

@Composable
fun AuthScreen(
    viewModelFactory: ViewModelFactory,
    viewModel: AuthUserViewModel = viewModel(
        modelClass = AuthUserViewModel::class.java,
        factory = viewModelFactory
    ),
    navHostController: NavHostController
) {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = Root.Auth.Navigation.SignIn.route
        ) {
            composable(
                route = Root.Auth.Navigation.SignIn.route
            ) {
                SignInScreen(
                    viewModel = viewModel,
                    navHostController = navHostController,
                    navController = navController
                )
            }
            composable(
                route = Root.Auth.Navigation.SignUp.route
            ) {
                SignUpScreen(
                    authViewModel = viewModel,
                    viewModel = viewModel(
                        modelClass = SignUpViewModel::class.java,
                        factory = viewModelFactory
                    ),
                    navController = navController,
                    navHostController = navHostController
                )
            }
        }
    }
}
