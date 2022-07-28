package com.swaptech.meet.presentation.screen.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swaptech.meet.di.presentation.viewmodel.ViewModelFactory
import com.swaptech.meet.presentation.SPLASH_SCREEN_WAIT_TIME_MS
import com.swaptech.meet.presentation.navigation.Auth
import com.swaptech.meet.presentation.navigation.Home
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.navigation.nav_graph.authNavGraph
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
        Auth.route
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
            authNavGraph(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
            composable(
                route = Home.route
            ) {
                HomeScreen(
                    localUserViewModel = viewModel(
                        modelClass = LocalUserViewModel::class.java,
                        factory = viewModelFactory
                    ),
                    viewModelFactory = viewModelFactory,
                    rootNavController = navController
                )
            }
        }
    }
}
