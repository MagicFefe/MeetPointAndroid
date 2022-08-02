package com.swaptech.meet.presentation.screen.home.user_screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.swaptech.meet.R
import com.swaptech.meet.presentation.navigation.destination.Auth
import com.swaptech.meet.presentation.navigation.destination.User
import com.swaptech.meet.presentation.utils.FetchWithParam
import com.swaptech.meet.presentation.utils.UserHeader
import com.swaptech.meet.presentation.utils.VerticalScrollableContent
import com.swaptech.meet.presentation.utils.replaceTo
import com.swaptech.meet.presentation.utils.toByteArray
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel

@Composable
fun UserDetailsScreen(
    userId: String,
    localUserId: String,
    remoteUserViewModel: RemoteUserViewModel,
    localUserViewModel: LocalUserViewModel,
    nestedNavController: NavHostController,
    bottomBarNavController: NavHostController,
    rootNavController: NavHostController
) {
    FetchWithParam(
        param = userId,
        action = {
            remoteUserViewModel.getUserById(it)
        }
    ) { userById ->
        val scrollState = rememberScrollState()
        val userCanEdit by remember(localUserId, userById) {
            mutableStateOf(localUserId == userById.id)
        }
        VerticalScrollableContent(
            scrollState = scrollState,
            content = {
                IconButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        bottomBarNavController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserHeader(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(100.dp),
                        userName = userById.name,
                        userSurname = userById.surname,
                        profileImage = userById.image.toByteArray()
                    )
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        if(userById.about.isNotEmpty()) {
                            UserDetailItem(
                                profileDetailName = stringResource(id = R.string.about),
                                profileDetailContent = userById.about
                            )
                        }
                        UserDetailItem(
                            profileDetailName = stringResource(id = R.string.date_of_birth),
                            profileDetailContent = userById.dob
                        )
                        UserDetailItem(
                            profileDetailName = stringResource(id = R.string.email),
                            profileDetailContent = userById.email
                        )
                        UserDetailItem(
                            profileDetailName = stringResource(id = R.string.country),
                            profileDetailContent = userById.country
                        )
                        UserDetailItem(
                            profileDetailName = stringResource(id = R.string.city),
                            profileDetailContent = userById.city
                        )
                    }
                }
            },
            stickyBottomContent = {
                if (userCanEdit) {
                    Column {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            onClick = {
                                nestedNavController.navigate(User.Update.route) {
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Text(text = stringResource(R.string.update_user))
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 5.dp,
                                    end = 5.dp,
                                    bottom = 5.dp,
                                    top = 2.dp
                                ),
                            onClick = {
                                localUserViewModel.deleteLocalUserById(localUserId)
                                rootNavController.replaceTo(Auth.route)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.logout)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun UserDetailItem(
    profileDetailName: String,
    profileDetailContent: String
) {
    Column(
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        Text(
            text = profileDetailName,
            color = Color.Gray
        )
        Text(
            text = profileDetailContent,
            fontSize = 20.sp
        )
    }
}
