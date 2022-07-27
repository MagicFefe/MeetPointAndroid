package com.swaptech.meet.presentation.screen.home.user.detail

import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.screen.home.user.UserScreenViewModel
import com.swaptech.meet.presentation.utils.UserHeader
import com.swaptech.meet.presentation.utils.replaceTo

@Composable
fun UserDetailScreen(
    userId: String,
    localUserId: String,
    viewModel: UserScreenViewModel,
    nestedNavController: NavHostController,
    navHostController: NavHostController
) {
    LaunchedEffect(userId) {
        viewModel.getUserById(userId)
    }
    val userById = viewModel.userById
    val userCanEdit by remember(localUserId, userById) {
        mutableStateOf(localUserId == userById?.id)
    }
    userById?.let {
        val scrollableState = rememberScrollState()
        Column(

        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollableState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserHeader(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(100.dp),
                        profileName = userById.name,
                        profileSurname = userById.surname,
                        profileImage = Base64.decode(
                            userById.image,
                            Base64.DEFAULT
                        )
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
                if (userCanEdit) {
                    Column {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp),
                            onClick = {
                                nestedNavController.navigate(Root.Home.Navigation.User.Navigation.Update.route) {
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Text(text = stringResource(R.string.edit_user))
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
                                viewModel.deleteLocalUserById(localUserId)
                                navHostController.replaceTo(Root.Auth.route)
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
        }
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
