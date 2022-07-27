package com.swaptech.meet.presentation.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.swaptech.meet.di.presentation.viewmodel.ViewModelFactory
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.screen.home.meetpoint.MeetPointScreen
import com.swaptech.meet.presentation.screen.home.meetpoint.MeetPointScreenViewModel
import com.swaptech.meet.presentation.screen.home.user.UserScreen
import com.swaptech.meet.presentation.screen.home.user.UserScreenViewModel

@Composable
fun HomeScreen(
    localUser: UserResponse,
    viewModelFactory: ViewModelFactory,
    navHostController: NavHostController
) {
    val navController = rememberNavController()
    val homeScreens = listOf(
        Root.Home.Navigation.MeetPoints,
        Root.Home.Navigation.User
    )
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination
                homeScreens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = screen.route)
                        },
                        selected = currentDestination?.route?.contains(screen.route) ?: false,
                        onClick = {
                            val navRoute =
                                if (screen.route.contains(Root.Home.Navigation.User.route)) {
                                    "${screen.route}/${localUser.id}"
                                } else {
                                    screen.route
                                }
                            navController.navigate(navRoute) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Root.Home.Navigation.MeetPoints.route
        ) {
            composable(Root.Home.Navigation.MeetPoints.route) {
                MeetPointScreen(
                    localUser = localUser,
                    viewModel = viewModel(
                        modelClass = MeetPointScreenViewModel::class.java,
                        factory = viewModelFactory
                    )
                )
            }
            composable(
                route = "${Root.Home.Navigation.User.route}/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                UserScreen(
                    userId = backStackEntry.arguments?.getString("userId") ?: "",
                    localUserId = localUser.id,
                    viewModel = viewModel(
                        modelClass = UserScreenViewModel::class.java,
                        factory = viewModelFactory
                    ),
                    navHostController = navHostController
                )
            }
        }
    }
}