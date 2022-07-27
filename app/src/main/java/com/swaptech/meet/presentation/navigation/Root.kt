package com.swaptech.meet.presentation.navigation

import androidx.annotation.DrawableRes
import com.swaptech.meet.R

sealed class Root(open val route: String) {
    object Placeholder: Root("placeholder")
    object Country: Root("country")
    object Auth: Root("auth") {
        sealed class Navigation(override val route: String): Root(route) {
            object SignIn: Navigation("sign_in")
            object SignUp: Navigation("sign_up")
        }
    }
    object Home: Root("home") {
        sealed class Navigation(override val route: String, @DrawableRes val icon: Int): Root(route) {
            object MeetPoints: Navigation("meet_points", R.drawable.ic_launcher_foreground)
            object User: Navigation("user", R.drawable.ic_baseline_user_circle_24) {
                sealed class Navigation(override val route: String): Root(route) {
                    object Detail: User.Navigation("detail")
                    object Update: User.Navigation("update")
                }
            }
        }
    }
    companion object {
        const val route = "root"
    }
}
