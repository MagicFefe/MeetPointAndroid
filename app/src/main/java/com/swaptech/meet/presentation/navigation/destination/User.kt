package com.swaptech.meet.presentation.navigation.destination

sealed class User(val route: String) {
    object Details: User("details")
    object Update: User("update")
}
