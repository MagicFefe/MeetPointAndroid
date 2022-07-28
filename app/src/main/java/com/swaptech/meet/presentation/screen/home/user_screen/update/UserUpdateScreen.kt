package com.swaptech.meet.presentation.screen.home.user_screen.update

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.swaptech.meet.domain.user.model.UserUpdate
import com.swaptech.meet.presentation.MAX_NAME_SURNAME_LENGTH
import com.swaptech.meet.presentation.MIN_PASSWORD_LENGTH
import com.swaptech.meet.presentation.navigation.User
import com.swaptech.meet.presentation.utils.FetchWithParam
import com.swaptech.meet.presentation.utils.UserImage
import com.swaptech.meet.presentation.utils.VerticalScrollableContent
import com.swaptech.meet.presentation.utils.replaceTo
import com.swaptech.meet.presentation.utils.resizeToProfileImage
import com.swaptech.meet.presentation.utils.toBase64
import com.swaptech.meet.presentation.utils.toByteArray
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
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
        val scrollableState = rememberScrollState()
        var userImage by rememberSaveable {
            mutableStateOf(userById.image.toByteArray())
        }
        val countries by remember {
            mutableStateOf(
                Locale.getAvailableLocales()
                    .map { it.displayCountry }
                    .filterNot { it.isEmpty() }
            )
        }
        val (name, onNameChange) = rememberSaveable {
            mutableStateOf(userById.name)
        }
        val (city, onCityChange) = rememberSaveable {
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
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? ->
                uri?.let {
                    val unprocessedBitmap =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source = ImageDecoder.createSource(context.contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            @Suppress("DEPRECATION")
                            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        }
                    userImage = unprocessedBitmap.resizeToProfileImage().toByteArray()
                }
            }
        )
        VerticalScrollableContent(
            scrollState = scrollableState,
            content = {
                var countriesMenuExpanded by rememberSaveable {
                    mutableStateOf(false)
                }
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    onClick = {
                        nestedNavController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserImage(
                        modifier = Modifier
                            .clickable {
                                launcher.launch("image/*")
                            }
                            .size(100.dp),
                        userImage = userImage
                    )
                    OutlinedTextField(
                        modifier = Modifier.width(280.dp),
                        value = name,
                        onValueChange = onNameChange,
                        label = {
                            Text(text = stringResource(id = R.string.name))
                        },
                        singleLine = true,
                        maxLines = 1
                    )
                    OutlinedTextField(
                        modifier = Modifier.width(280.dp),
                        value = surname,
                        onValueChange = { input ->
                            surname = input.trim()
                        },
                        label = {
                            Text(text = stringResource(id = R.string.surname))
                        },
                        singleLine = true,
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { input ->
                            email = input.trim()
                        },
                        label = {
                            Text(text = stringResource(id = R.string.email))
                        }
                    )
                    ExposedDropdownMenuBox(
                        expanded = countriesMenuExpanded,
                        onExpandedChange = {
                            countriesMenuExpanded = !countriesMenuExpanded
                        }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.width(280.dp),
                            readOnly = true,
                            value = country,
                            onValueChange = {
                            },
                            label = {
                                Text(text = stringResource(id = R.string.country))
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = countriesMenuExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = countriesMenuExpanded,
                            onDismissRequest = {
                                countriesMenuExpanded = false
                            }
                        ) {
                            //TODO: Create screen with country list
                            countries.forEach { menuCountry ->
                                DropdownMenuItem(
                                    onClick = {
                                        country = menuCountry
                                        countriesMenuExpanded = false
                                    }
                                ) {
                                    Text(text = menuCountry)
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        modifier = Modifier.width(280.dp),
                        value = city,
                        onValueChange = onCityChange,
                        label = {
                            Text(text = stringResource(id = R.string.city))
                        },
                        singleLine = true,
                        maxLines = 1
                    )
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
            },
            stickyBottomContent = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    onClick = {
                        if (name.isEmpty() || surname.isEmpty() || email.isEmpty()
                            || country.isEmpty() || city.isEmpty() || oldPassword.isEmpty()
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
                        if (oldPassword.length < MIN_PASSWORD_LENGTH) {
                            Toast.makeText(
                                context,
                                R.string.password_too_short_message,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (newPassword.isNotEmpty()) {
                            if (newPassword.length < MIN_PASSWORD_LENGTH) {
                                Toast.makeText(
                                    context,
                                    R.string.password_too_short_message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(
                                context,
                                R.string.incorrect_password,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        if (name.length > MAX_NAME_SURNAME_LENGTH
                            || surname.length > MAX_NAME_SURNAME_LENGTH
                        ) {
                            val message = context.getString(
                                R.string.max_name_surname_length_limit_exceeded,
                                MAX_NAME_SURNAME_LENGTH.toString()
                            )
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            return@Button
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
        )
    }
}
