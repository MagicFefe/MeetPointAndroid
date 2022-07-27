package com.swaptech.meet.presentation.screen.home.user

sealed class UserScreenState {
    object Idle: UserScreenState()
    object Update: UserScreenState()
}
