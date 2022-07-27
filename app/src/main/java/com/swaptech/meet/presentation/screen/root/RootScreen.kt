package com.swaptech.meet.presentation.screen.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.swaptech.meet.di.presentation.viewmodel.ViewModelFactory
import com.swaptech.meet.presentation.SPLASH_SCREEN_WAIT_TIME_MS
import com.swaptech.meet.presentation.navigation.Auth
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.screen.auth.AuthScreen
import com.swaptech.meet.presentation.screen.home.HomeScreen
import com.swaptech.meet.presentation.screen.placeholder.PlaceholderScreen
import com.swaptech.meet.presentation.utils.replaceTo
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import kotlinx.coroutines.delay

@Composable
fun RootScreen(
    viewModelFactory: ViewModelFactory,
    viewModel: LocalUserViewModel
) {
    val navController = rememberNavController()
    val user by viewModel.localUser.collectAsState(null)
    val startDestination = if (user == null) {
        Root.Auth.route
    } else {
        Root.Home.route
    }
    LaunchedEffect(startDestination) {
        delay(SPLASH_SCREEN_WAIT_TIME_MS)
        navController.replaceTo(startDestination)
    }
    Scaffold { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Root.Placeholder.route,
            route = Root.route
        ) {
            composable(
                route = Root.Placeholder.route
            ) {
                PlaceholderScreen()
            }
            composable(
                route = Root.Auth.route
            ) {
                AuthScreen(
                    viewModelFactory = viewModelFactory,
                    navHostController = navController
                )
            }
            composable(
                route = Root.Home.route
            ) {
                //Getting user from db again for calling recomposition
                val localUser by viewModel.localUser.collectAsState(null)
                localUser?.let {
                    HomeScreen(
                        localUser = it,
                        viewModelFactory = viewModelFactory,
                        navHostController = navController
                    )
                }
            }
        }
    }
}
