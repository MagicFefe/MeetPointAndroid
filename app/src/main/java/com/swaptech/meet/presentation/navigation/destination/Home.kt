package com.swaptech.meet.presentation.navigation.destination

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.swaptech.meet.R

/**
 * it using only for describing navigation between bottom bar destination
 * (excludes nested navigation in bottom bar destination)
 */
sealed class Home(open val route: String, @StringRes val name: Int, val icon: ImageVector) {
    object MeetPoints : Home("meet_points", R.string.meet_points, Icons.Outlined.Map)
    object More : Home("more", R.string.more, Icons.Outlined.MoreHoriz)
    companion object {
        const val route = "home"
    }
}


