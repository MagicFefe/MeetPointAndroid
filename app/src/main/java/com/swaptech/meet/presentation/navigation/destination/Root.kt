package com.swaptech.meet.presentation.navigation.destination

sealed class Root(open val route: String) {
    object Placeholder : Root("placeholder")
    object Home : Root("home")
    object UserScreen: Root("user_screen/{userId}") {
        private const val rootRouteName = "user_screen"
        fun getNavigationRoute(userId: String) = "$rootRouteName/$userId"
    }
    companion object {
        const val route = "root"
    }
}
