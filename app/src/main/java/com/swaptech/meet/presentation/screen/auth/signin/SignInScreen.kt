package com.swaptech.meet.presentation.screen.auth.signin

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.swaptech.meet.R
import com.swaptech.meet.domain.user.model.UserMinimal
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.screen.auth.AuthUserViewModel
import com.swaptech.meet.presentation.utils.replaceTo

@Composable
fun SignInScreen(
    viewModel: AuthUserViewModel,
    navHostController: NavHostController,
    navController: NavController
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = email,
                onValueChange = { input ->
                    email = input.trim()
                },
                label = {
                    Text(text = stringResource(id = R.string.email))
                },
                singleLine = true,
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = password,
                onValueChange = { input ->
                    password = input.trim()
                },
                label = {
                    Text(text = stringResource(id = R.string.password))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1
            )
            Row(
                modifier = Modifier
                    .width(280.dp)
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(
                                context,
                                R.string.all_fields_must_be_filled,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (password.length < 8) {
                            Toast.makeText(
                                context,
                                R.string.password_too_short_message,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(
                                context,
                                R.string.email_is_invalid,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        val user = UserMinimal(
                            email = email,
                            password = password
                        )
                        viewModel.signInUser(
                            user = user,
                            onSuccess = {
                                navHostController.replaceTo(Root.Home.route)
                            },
                            onHttpError = { error ->
                                when (error.code()) {
                                    404 -> {
                                        Toast.makeText(
                                            context,
                                            error.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    422 -> {
                                        Toast.makeText(
                                            context,
                                            error.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }
                ) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.dont_have_an_account),
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.padding(2.dp))
            ClickableText(
                text = AnnotatedString(
                    text = stringResource(id = R.string.sign_up),
                    spanStyle = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                onClick = {
                    navController.navigate(Root.Auth.Navigation.SignUp.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
