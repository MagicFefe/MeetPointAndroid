package com.swaptech.meet.presentation.screen.home.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Help
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.swaptech.meet.R
import com.swaptech.meet.presentation.navigation.destination.Root
import com.swaptech.meet.presentation.utils.FetchWithParam
import com.swaptech.meet.presentation.utils.Separator
import com.swaptech.meet.presentation.utils.UserHeader
import com.swaptech.meet.presentation.utils.navigateSingle
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
        action = { remoteUserViewModel.getUserById(it) },
        onLoading = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    ) { userById ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .clickable {
                        nestedNavController.navigateSingle(
                            Root.UserScreen.getNavigationRoute(
                                localUserId
                            )
                        )
                    }
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserHeader(
                    modifier = Modifier.size(100.dp),
                    userName = userById.name,
                    userSurname = userById.surname,
                    profileImage = userById.image.toByteArray()
                )
            }
            Separator()
            MoreScreenListItem(
                onItemClick = {
                    nestedNavController.navigateSingle(Root.Feedback.route)
                },
                text = stringResource(id = R.string.feedback),
                icon = Icons.Outlined.Chat
            )
            MoreScreenListItem(
                onItemClick = {
                    nestedNavController.navigateSingle(Root.About.route)
                },
                text = stringResource(id = R.string.about_app),
                icon = Icons.Outlined.Help
            )
        }
    }
}

@Composable
fun MoreScreenListItem(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    text: String,
    icon: ImageVector
) {
    Row(
        modifier = modifier
            .clickable(onClick = onItemClick)
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(text = text)
    }
}
