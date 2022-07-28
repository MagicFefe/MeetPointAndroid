package com.swaptech.meet.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.swaptech.meet.R

sealed class Home(open val route: String, @StringRes val name: Int, val icon: ImageVector) {
    object MeetPoints : Home("meet_points", R.string.meet_points, Icons.Outlined.Map)
    object More : Home("more", R.string.more, Icons.Outlined.MoreHoriz)
    companion object {
        const val route = "home"
    }
}


