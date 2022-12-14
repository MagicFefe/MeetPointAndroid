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
import com.swaptech.meet.presentation.MAX_ABOUT_FIELD_LENGTH
import com.swaptech.meet.presentation.MAX_CITY_NAME_LENGTH
import com.swaptech.meet.presentation.DOB_LENGTH
import com.swaptech.meet.presentation.MAX_EMAIL_LENGTH
import com.swaptech.meet.presentation.MAX_NAME_SURNAME_LENGTH
import com.swaptech.meet.presentation.MIN_YEARS_COUNT
import com.swaptech.meet.presentation.navigation.destination.User
import com.swaptech.meet.presentation.utils.FetchWithParam
import com.swaptech.meet.presentation.utils.LoadingPlaceholder
import com.swaptech.meet.presentation.utils.UpdateSignUpUserForm
import com.swaptech.meet.presentation.utils.Validator
import com.swaptech.meet.presentation.utils.formatToDate
import com.swaptech.meet.presentation.utils.network_error_handling.handleError
import com.swaptech.meet.presentation.utils.replaceTo
import com.swaptech.meet.presentation.utils.toBase64
import com.swaptech.meet.presentation.utils.toByteArray
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel

@Composable
fun UserUpdateScreen(
    userId: String,
    remoteUserViewModel: RemoteUserViewModel,
    localUserViewModel: LocalUserViewModel,
    viewModel: UserUpdateScreenViewModel,
    nestedNavController: NavHostController
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
        userById?.let {
            var userImage by rememberSaveable {
                mutableStateOf(userById.image.toByteArray())
            }
            var name by rememberSaveable {
                mutableStateOf(userById.name)
            }
            var city by rememberSaveable {
                mutableStateOf(userById.city)
            }
            var gender by rememberSaveable {
                mutableStateOf(userById.gender)
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
            var about by rememberSaveable {
                mutableStateOf(userById.about)
            }
            var date by rememberSaveable {
                mutableStateOf(
                    userById.dob.split("-").fold("") { start, item -> start + item }
                )
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
                gender = gender,
                onGenderChooserItemClick = { selected ->
                    gender = selected
                },
                about = about,
                onAboutChange = { input ->
                    if (input.length <= MAX_ABOUT_FIELD_LENGTH) {
                        about = input
                    }
                },
                date = date,
                onDateChange = { input ->
                    if (input.length <= DOB_LENGTH) {
                        date = input
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
                                    name, surname, email, country, city, oldPassword, date, gender
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    R.string.only_new_password_and_about_fields_can_be_empty,
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
                            if (date.length == DOB_LENGTH) {
                                if (Validator.dateIsNotValid(date.formatToDate())) {
                                    Toast.makeText(
                                        context,
                                        R.string.enter_correct_date,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                val ageInvalid = Validator.ageIsNotValid(
                                    date.formatToDate(),
                                    onAgeIsTooSmall = {
                                        val message = context.getString(
                                            R.string.age_is_too_small_message,
                                            MIN_YEARS_COUNT.toString()
                                        )
                                        Toast.makeText(
                                            context,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onAgeIsTooBig = {
                                        Toast.makeText(
                                            context,
                                            R.string.you_are_too_old,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                                if (ageInvalid) {
                                    return@Button
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    R.string.enter_correct_date,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                return@Button
                            }
                            if (Validator.aboutFieldIsNotValid(about)
                            ) {
                                Toast.makeText(
                                    context,
                                    R.string.about_is_too_long,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
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
                                email = email,
                                oldPassword = oldPassword,
                                newPassword = newPassword.ifEmpty {
                                    oldPassword
                                },
                                id = userById.id,
                                name = name,
                                surname = surname,
                                dob = date.formatToDate(),
                                gender = gender,
                                about = about,
                                country = country,
                                city = city,
                                image = String(
                                    userImage.toBase64()
                                )
                            )
                            viewModel.updateUser(
                                user = userUpdate,
                                onSuccess = { newUser ->
                                    localUserViewModel.saveLocalUser(newUser)
                                    nestedNavController.replaceTo(User.Details.route)
                                },
                                onFail = { _, error ->
                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
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
}
