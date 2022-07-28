package com.swaptech.meet.presentation.screen.home.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.navigation.User
import com.swaptech.meet.presentation.utils.FetchWithParam
import com.swaptech.meet.presentation.utils.UserImage
import com.swaptech.meet.presentation.utils.toByteArray
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel

@Composable
fun MoreScreen(
    localUserId: String,
    remoteUserViewModel: RemoteUserViewModel,
    nestedNavController: NavController
) {
    FetchWithParam(
        param = localUserId,
        action = { remoteUserViewModel.getUserById(it) }
    ) { userById ->
        Column(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .clickable {
                        nestedNavController.navigate(Root.UserScreen.getNavigationRoute(localUserId)) {
                            launchSingleTop = true
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserImage(
                    modifier = Modifier.size(50.dp),
                    userImage = userById.image.toByteArray()
                )
                Spacer(modifier = Modifier.width(2.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = userById.name)
                    Text(text = userById.surname)
                }
            }
        }
    }
}
