package com.swaptech.meet.presentation.navigation.destination

sealed class Auth(val route: String) {
    object SignUp: Auth("sign_up")
    object SignIn: Auth("sign_in")
    companion object {
        const val route = "auth"
    }
}
