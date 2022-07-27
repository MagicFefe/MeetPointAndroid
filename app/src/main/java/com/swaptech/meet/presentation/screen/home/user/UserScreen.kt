package com.swaptech.meet.presentation.screen.home.user

import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swaptech.meet.R
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.screen.home.user.detail.UserDetailScreen
import com.swaptech.meet.presentation.screen.home.user.update.UserUpdateScreen
import com.swaptech.meet.presentation.utils.UserHeader
import com.swaptech.meet.presentation.utils.UserImage

@Composable
fun UserScreen(
    userId: String,
    localUserId: String,
    viewModel: UserScreenViewModel,
    navHostController: NavHostController
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Root.Home.Navigation.User.Navigation.Detail.route
    ) {
        composable(
            route = Root.Home.Navigation.User.Navigation.Detail.route
        ) {
            UserDetailScreen(
                userId = userId,
                localUserId = localUserId,
                viewModel = viewModel,
                nestedNavController = navController,
                navHostController = navHostController
            )
        }
        composable(
            route = Root.Home.Navigation.User.Navigation.Update.route
        ) {
            UserUpdateScreen(
                userId = userId,
                localUserId = localUserId,
                viewModel = viewModel,
                nestedNavController = navController,
                navHostController = navHostController
            )
        }
    }
}
