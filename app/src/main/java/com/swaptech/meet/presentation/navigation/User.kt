package com.swaptech.meet.presentation.navigation

sealed class User(val route: String) {
    object Details: User("details")
    object Update: User("update")
}
