package com.swaptech.meet.presentation.screen.auth.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.swaptech.meet.R
import com.swaptech.meet.domain.auth.SignUp
import com.swaptech.meet.presentation.DOB_LENGTH
import com.swaptech.meet.presentation.MAX_ABOUT_FIELD_LENGTH
import com.swaptech.meet.presentation.MAX_CITY_NAME_LENGTH
import com.swaptech.meet.presentation.MAX_EMAIL_LENGTH
import com.swaptech.meet.presentation.MAX_NAME_SURNAME_LENGTH
import com.swaptech.meet.presentation.MIN_YEARS_COUNT
import com.swaptech.meet.presentation.navigation.destination.Root
import com.swaptech.meet.presentation.screen.auth.AuthUserViewModel
import com.swaptech.meet.presentation.utils.UpdateSignUpUserForm
import com.swaptech.meet.presentation.utils.Validator
import com.swaptech.meet.presentation.utils.formatToDate
import com.swaptech.meet.presentation.utils.network_error_handling.handleError
import com.swaptech.meet.presentation.utils.replaceTo
import com.swaptech.meet.presentation.utils.toBase64

@Composable
fun SignUpScreen(
    authUserViewModel: AuthUserViewModel,
    navController: NavHostController
) {
    var userImage: ByteArray? by rememberSaveable {
        mutableStateOf(null)
    }
    var name by rememberSaveable {
        mutableStateOf("")
    }
    var surname by rememberSaveable {
        mutableStateOf("")
    }
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var country by rememberSaveable {
        mutableStateOf("")
    }
    var city by rememberSaveable {
        mutableStateOf("")
    }
    var gender by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var about by rememberSaveable {
        mutableStateOf("")
    }
    var date by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current
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
            if (input.length <= MAX_CITY_NAME_LENGTH) {
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
        onImageChooseResult = { selectedImage ->
            userImage = selectedImage
        },
        onCloseButtonClick = {
            navController.popBackStack()
        }
    ) {
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
                    if (
                        Validator.anyFieldIsEmpty(
                            name, surname, email, country, city, password, date, gender
                        )
                    ) {
                        Toast.makeText(
                            context,
                            R.string.all_fields_must_be_filled,
                            Toast.LENGTH_SHORT
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
                            R.string.email_is_invalid,
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
                        Toast.makeText(context, R.string.enter_correct_date, Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }
                    if (Validator.aboutFieldIsNotValid(about)
                    ) {
                        Toast.makeText(context, R.string.about_is_too_long, Toast.LENGTH_LONG)
                            .show()
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
                    val signUp = SignUp(
                        name = name,
                        surname = surname,
                        about = about,
                        dob = date.formatToDate(),
                        gender = gender,
                        email = email,
                        country = country,
                        city = city,
                        password = password,
                        image = userImage?.let { byteArray ->
                            String(
                                byteArray.toBase64()
                            )
                        }
                    )
                    authUserViewModel.signUpUser(
                        signUp = signUp,
                        onSuccess = {
                            navController.replaceTo(Root.Home.route)
                        },
                        onFail = { _, message ->
                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_LONG
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
                Text(text = stringResource(id = R.string.sign_up))
            }
        }
    }
}
