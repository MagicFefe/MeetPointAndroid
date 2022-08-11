package com.swaptech.meet.presentation.screen.auth.signin

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.swaptech.meet.R
import com.swaptech.meet.domain.auth.SignIn
import com.swaptech.meet.presentation.navigation.destination.Auth
import com.swaptech.meet.presentation.navigation.destination.Root
import com.swaptech.meet.presentation.screen.auth.AuthUserViewModel
import com.swaptech.meet.presentation.utils.Validator
import com.swaptech.meet.presentation.utils.network_error_handling.handleError
import com.swaptech.meet.presentation.utils.replaceTo

@Composable
fun SignInScreen(
    viewModel: AuthUserViewModel,
    navController: NavHostController
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
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
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
                        if (Validator.anyFieldIsEmpty(email, password)) {
                            Toast.makeText(
                                context,
                                R.string.all_fields_must_be_filled,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (Validator.passwordIsNotValid(password)) {
                            Toast.makeText(
                                context,
                                R.string.password_too_short_message,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (Validator.emailIsNotValid(email)) {
                            Toast.makeText(
                                context,
                                R.string.email_is_invalid,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        val user = SignIn(
                            email = email,
                            password = password
                        )
                        viewModel.signInUser(
                            user = user,
                            onSuccess = {
                                navController.replaceTo(Root.Home.route)
                            },
                            onFail = { _, message ->
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
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
                    navController.navigate(Auth.SignUp.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
