package com.swaptech.meet.presentation.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.replaceTo(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route) {
        launchSingleTop = true
        popBackStack()
        builder()
    }
}
