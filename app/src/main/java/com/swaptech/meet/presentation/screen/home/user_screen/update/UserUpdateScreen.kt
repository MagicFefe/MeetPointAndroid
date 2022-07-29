package com.swaptech.meet.presentation.screen.home.user_screen.update

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.swaptech.meet.R
import com.swaptech.meet.domain.user.model.UserUpdate
import com.swaptech.meet.presentation.MAX_CITY_NAME_LENGTH
import com.swaptech.meet.presentation.MAX_EMAIL_LENGTH
import com.swaptech.meet.presentation.MAX_NAME_SURNAME_LENGTH
import com.swaptech.meet.presentation.navigation.destination.User
import com.swaptech.meet.presentation.utils.FetchWithParam
import com.swaptech.meet.presentation.utils.UpdateSignUpUserForm
import com.swaptech.meet.presentation.utils.Validator
import com.swaptech.meet.presentation.utils.replaceTo
import com.swaptech.meet.presentation.utils.toBase64
import com.swaptech.meet.presentation.utils.toByteArray
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel

@Composable
fun UserUpdateScreen(
    userId: String,
    localUserId: String,
    remoteUserViewModel: RemoteUserViewModel,
    localUserViewModel: LocalUserViewModel,
    viewModel: UserUpdateScreenViewModel,
    nestedNavController: NavHostController
) {
    FetchWithParam(
        param = userId,
        action = { remoteUserViewModel.getUserById(it) }
    ) { userById ->
        val context = LocalContext.current
        var userImage by rememberSaveable {
            mutableStateOf(userById.image.toByteArray())
        }
        var name by rememberSaveable {
            mutableStateOf(userById.name)
        }
        var city by rememberSaveable {
            mutableStateOf(userById.city)
        }
        var surname by rememberSaveable {
            mutableStateOf(userById.surname)
        }
        var email by rememberSaveable {
            mutableStateOf(userById.email)
        }
        var country by rememberSaveable {
            mutableStateOf(userById.country)
        }
        var oldPassword by rememberSaveable {
            mutableStateOf("")
        }
        var newPassword by rememberSaveable {
            mutableStateOf("")
        }
        UpdateSignUpUserForm(
            name = name,
            onNameChange = { input ->
                if (input.length <= MAX_NAME_SURNAME_LENGTH) {
                    name = input.trim()
                }
            },
            surname = surname,
            onSurnameChange = { input ->
                if (input.length <= MAX_NAME_SURNAME_LENGTH) {
                    surname = input.trim()
                }
            },
            email = email,
            onEmailChange = { input ->
                if (input.length <= MAX_EMAIL_LENGTH) {
                    email = input.trim()
                }

            },
            country = country,
            onCountryClick = { selected ->
                country = selected
            },
            city = city,
            onCityChange = { input ->
                if (input.length < MAX_CITY_NAME_LENGTH) {
                    city = input
                }
            },
            image = userImage,
            onImageChooseResult = { selectedImage ->
                selectedImage?.let {
                    userImage = selectedImage
                }
            },
            onCloseButtonClick = {
                nestedNavController.popBackStack()
            },
            finishButton = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    onClick = {
                        if (
                            Validator.anyFieldIsEmpty(
                                name, surname, email, country, city, oldPassword
                            )
                        ) {
                            val newPasswordString = context.getString(R.string.new_password)
                            val message = context.getString(
                                R.string.only_field_name_can_be_empty,
                                newPasswordString
                            )
                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }
                        if (Validator.nameSurnameIsNotValid(name)
                            || Validator.nameSurnameIsNotValid(surname)
                        ) {
                            val message = context.getString(
                                R.string.max_name_surname_length_limit_exceeded,
                                MAX_NAME_SURNAME_LENGTH.toString()
                            )
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        if (Validator.emailIsNotValid(email)) {
                            Toast.makeText(
                                context,
                                R.string.incorrect_password,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (Validator.cityIsNotValid(city)) {
                            Toast.makeText(
                                context,
                                R.string.city_name_is_too_long,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (Validator.passwordIsNotValid(oldPassword)) {
                            Toast.makeText(
                                context,
                                R.string.password_too_short_message,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (newPassword.isNotEmpty()) {
                            if (Validator.passwordIsNotValid(newPassword)) {
                                Toast.makeText(
                                    context,
                                    R.string.password_too_short_message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                        }
                        if (oldPassword == newPassword) {
                            Toast.makeText(
                                context,
                                R.string.new_and_old_password_the_same_message,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        val userUpdate = UserUpdate(
                            id = userById.id,
                            name = name,
                            surname = surname,
                            newEmail = email,
                            oldEmail = userById.email,
                            country = country,
                            city = city,
                            oldPassword = oldPassword,
                            newPassword = newPassword.ifEmpty {
                                oldPassword
                            },
                            image = String(
                                userImage.toBase64()
                            )
                        )
                        viewModel.updateUser(
                            user = userUpdate,
                            onSuccess = { newUser ->
                                localUserViewModel.deleteLocalUserById(localUserId)
                                localUserViewModel.saveLocalUser(newUser)
                                nestedNavController.replaceTo(User.Details.route)
                            },
                            onHttpError = { error ->
                                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                ) {
                    Text(text = stringResource(R.string.done))
                }
            }
        ) {
            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = oldPassword,
                onValueChange = { input ->
                    oldPassword = input.trim()
                },
                label = {
                    Text(text = stringResource(id = R.string.enter_old_password))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1
            )
            OutlinedTextField(
                modifier = Modifier.width(280.dp),
                value = newPassword,
                onValueChange = { input ->
                    newPassword = input.trim()
                },
                label = {
                    Text(text = stringResource(id = R.string.enter_new_password))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1
            )
        }
    }
}
