package com.swaptech.meet.presentation.screen.home.user_screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swaptech.meet.di.presentation.viewmodel.ViewModelFactory
import com.swaptech.meet.presentation.navigation.User
import com.swaptech.meet.presentation.screen.home.user_screen.details.UserDetailsScreen
import com.swaptech.meet.presentation.screen.home.user_screen.update.UserUpdateScreen
import com.swaptech.meet.presentation.screen.home.user_screen.update.UserUpdateScreenViewModel
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel


@Composable
fun UserScreen(
    clickedUserId: String,
    localUserId: String,
    rootNavController: NavHostController,
    bottomBarNavController: NavHostController,
    viewModelFactory: ViewModelFactory
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = User.Details.route
    ) {
        composable(
            route = User.Details.route
        ) {
            UserDetailsScreen(
                userId = clickedUserId,
                localUserId = localUserId,
                remoteUserViewModel = viewModel(
                    modelClass = RemoteUserViewModel::class.java,
                    factory = viewModelFactory
                ),
                localUserViewModel = viewModel(
                    modelClass = LocalUserViewModel::class.java,
                    factory = viewModelFactory
                ),
                nestedNavController = navController,
                bottomBarNavController = bottomBarNavController,
                rootNavController = rootNavController
            )
        }
        composable(
            route = User.Update.route
        ) {
            UserUpdateScreen(
                userId = clickedUserId,
                localUserId = localUserId,
                viewModel = viewModel(
                    modelClass = UserUpdateScreenViewModel::class.java,
                    factory = viewModelFactory
                ),
                localUserViewModel = viewModel(
                    modelClass = LocalUserViewModel::class.java,
                    factory = viewModelFactory
                ),
                remoteUserViewModel = viewModel(
                    modelClass = RemoteUserViewModel::class.java,
                    factory = viewModelFactory
                ),
                nestedNavController = navController
            )
        }
    }
}


