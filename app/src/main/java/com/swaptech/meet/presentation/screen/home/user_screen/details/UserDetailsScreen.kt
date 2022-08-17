package com.swaptech.meet.presentation.screen.home.user_screen.details

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.swaptech.meet.R
import com.swaptech.meet.domain.user.model.UserResponse
import com.swaptech.meet.presentation.navigation.destination.Auth
import com.swaptech.meet.presentation.navigation.destination.User
import com.swaptech.meet.presentation.utils.FetchWithParam
import com.swaptech.meet.presentation.utils.LoadingPlaceholder
import com.swaptech.meet.presentation.utils.Separator
import com.swaptech.meet.presentation.utils.UserHeader
import com.swaptech.meet.presentation.utils.VerticalScrollableContent
import com.swaptech.meet.presentation.utils.network_error_handling.handleError
import com.swaptech.meet.presentation.utils.replaceTo
import com.swaptech.meet.presentation.utils.toByteArray
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel

@Composable
fun UserDetailsScreen(
    userId: String,
    localUser: UserResponse,
    remoteUserViewModel: RemoteUserViewModel,
    localUserViewModel: LocalUserViewModel,
    userDetailsScreenViewModel: UserDetailsScreenViewModel,
    nestedNavController: NavHostController,
    bottomBarNavController: NavHostController,
    rootNavController: NavHostController
) {
    val context = LocalContext.current
    FetchWithParam(
        param = userId,
        action = {
            remoteUserViewModel.getUserById(
                it,
                onFail = { _, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                },
                onError = { error ->
                    handleError(
                        error,
                        onConnectionFault = {
                            Toast.makeText(
                                context,
                                R.string.no_internet_connection,
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onSocketTimeout = {
                            Toast.makeText(
                                context,
                                R.string.remote_services_unavailable,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            )
        },
        onLoading = {
            LoadingPlaceholder()
        }
    ) { userById ->
        val scrollState = rememberScrollState()
        val userTheSame by remember(localUser.id, userById) {
            mutableStateOf(localUser.id == userId)
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
                val user by remember {
                    mutableStateOf(
                        userById ?: kotlin.run {
                            if(userTheSame) {
                                localUser
                            } else {
                                null
                            }
                        }
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    user?.let {
                        UserHeader(
                            modifier = Modifier
                                .padding(10.dp)
                                .size(100.dp),
                            userName = it.name,
                            userSurname = it.surname,
                            profileImage = it.image.toByteArray()
                        )
                        Separator()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            if (it.about.isNotEmpty()) {
                                UserDetailItem(
                                    profileDetailName = stringResource(id = R.string.about),
                                    profileDetailContent = it.about
                                )
                            }
                            UserDetailItem(
                                profileDetailName = stringResource(id = R.string.gender),
                                profileDetailContent = it.gender
                            )
                            UserDetailItem(
                                profileDetailName = stringResource(id = R.string.date_of_birth),
                                profileDetailContent = it.dob
                            )
                            UserDetailItem(
                                profileDetailName = stringResource(id = R.string.email),
                                profileDetailContent = it.email
                            )
                            UserDetailItem(
                                profileDetailName = stringResource(id = R.string.country),
                                profileDetailContent = it.country
                            )
                            UserDetailItem(
                                profileDetailName = stringResource(id = R.string.city),
                                profileDetailContent = it.city
                            )
                        }
                    }
                }
            },
            stickyBottomContent = {
                if (userTheSame) {
                    Column {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            onClick = {
                                nestedNavController.navigate(User.Update.route) {
                                    launchSingleTop = true
                                }
                            },
                            enabled = userById != null
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
                                localUserViewModel.deleteLocalUserById(localUser.id)
                                userDetailsScreenViewModel.deleteAll()
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
