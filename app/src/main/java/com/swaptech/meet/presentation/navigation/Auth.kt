package com.swaptech.meet.presentation.navigation

sealed class Auth(open val route: String) {
    object SignUp: Auth("sign_up")
    object SignIn: Auth("sign_in")
    companion object {
        const val route = "auth"
    }
}
