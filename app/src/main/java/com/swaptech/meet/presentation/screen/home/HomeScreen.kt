package com.swaptech.meet.presentation.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.swaptech.meet.presentation.navigation.destination.Home
import com.swaptech.meet.presentation.navigation.destination.Root
import com.swaptech.meet.presentation.screen.home.meetpoint.MeetPointScreenViewModel
import com.swaptech.meet.presentation.screen.home.meetpoint.MeetPointsScreen
import com.swaptech.meet.presentation.screen.home.more.MoreScreen
import com.swaptech.meet.presentation.screen.home.more.about.AboutScreen
import com.swaptech.meet.presentation.screen.home.more.feedback.FeedbackScreen
import com.swaptech.meet.presentation.screen.home.more.feedback.FeedbackScreenViewModel
import com.swaptech.meet.presentation.screen.home.user_screen.UserScreen
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel

@Composable
fun HomeScreen(
    localUserViewModel: LocalUserViewModel,
    viewModelFactory: ViewModelFactory,
    rootNavController: NavHostController
) {
    //Getting user from db again for calling recomposition
    val savedUser by localUserViewModel.localUser.collectAsState(null)
    val bottomBarNavController = rememberNavController()
    val homeScreens = listOf(
        Home.MeetPoints,
        Home.More
    )
    savedUser?.let { localUser ->
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val backStackEntry by bottomBarNavController.currentBackStackEntryAsState()
                    val currentDestination = backStackEntry?.destination
                    homeScreens.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(text = stringResource(screen.name))
                            },
                            selected = currentDestination?.route?.contains(screen.route) ?: false,
                            onClick = {
                                val navRoute =
                                    if (screen.route.contains(Home.More.route)) {
                                        screen.route
                                    } else {
                                        screen.route
                                    }
                                bottomBarNavController.navigate(navRoute) {
                                    popUpTo(bottomBarNavController.graph.findStartDestination().id)
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
                route = Root.Home.route,
                navController = bottomBarNavController,
                startDestination = Home.MeetPoints.route
            ) {
                composable(Home.MeetPoints.route) {
                    MeetPointsScreen(
                        localUser = localUser,
                        viewModel = viewModel(
                            modelClass = MeetPointScreenViewModel::class.java,
                            factory = viewModelFactory
                        ),
                        nestedNavController = bottomBarNavController
                    )
                }
                composable(Home.More.route) {
                    MoreScreen(
                        localUserId = localUser.id,
                        remoteUserViewModel = viewModel(
                            modelClass = RemoteUserViewModel::class.java,
                            factory = viewModelFactory
                        ),
                        nestedNavController = bottomBarNavController
                    )
                }

                composable(
                    route = Root.UserScreen.route,
                    arguments = listOf(
                        navArgument("userId") { type = NavType.StringType }
                    )
                ) {
                    val userId = bottomBarNavController
                        .currentBackStackEntry?.arguments?.getString("userId")
                    val localUserId = localUser.id
                    userId?.let {
                        UserScreen(
                            clickedUserId = userId,
                            localUserId = localUserId,
                            rootNavController = rootNavController,
                            bottomBarNavController = bottomBarNavController,
                            viewModelFactory = viewModelFactory
                        )
                    }
                }
                composable(
                    route = Root.Feedback.route
                ) {
                    FeedbackScreen(
                        userId = localUser.id,
                        feedbackScreenViewModel = viewModel(
                            modelClass = FeedbackScreenViewModel::class.java,
                            factory = viewModelFactory
                        ),
                        nestedNavController = bottomBarNavController
                    )
                }
                composable(route = Root.About.route) {
                    AboutScreen()
                }
            }
        }
    }
}
