package com.swaptech.meet.presentation.screen.auth.signup

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.swaptech.meet.R
import com.swaptech.meet.domain.user.model.UserRegister
import com.swaptech.meet.presentation.MAX_NAME_SURNAME_LENGTH
import com.swaptech.meet.presentation.MIN_PASSWORD_LENGTH
import com.swaptech.meet.presentation.navigation.Root
import com.swaptech.meet.presentation.screen.auth.AuthUserViewModel
import com.swaptech.meet.presentation.utils.replaceTo
import com.swaptech.meet.presentation.utils.resizeToProfileImage
import com.swaptech.meet.presentation.utils.toByteArray
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignUpScreen(
    authViewModel: AuthUserViewModel,
    viewModel: SignUpViewModel,
    navHostController: NavHostController,
    navController: NavController
) {
    var imageBitmap: Bitmap? by rememberSaveable {
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
    val (city, onCityChange) = rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var countriesMenuExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val countries by remember {
        mutableStateOf(
            Locale.getAvailableLocales()
                .map { it.displayCountry }
                .filterNot { it.isEmpty() }
        )
    }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    imageBitmap = ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
            }
        }
    )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            onClick = {
                navController.navigate(Root.Auth.Navigation.SignIn.route) {
                    launchSingleTop = true
                    popUpTo(Root.Auth.Navigation.SignIn.route)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val unprocessedUserImage = (imageBitmap?.asImageBitmap() ?: kotlin.run {
                        ImageBitmap.imageResource(R.drawable.user_image_placeholder)
                    }).asAndroidBitmap()
                    val userImage = unprocessedUserImage
                        .resizeToProfileImage()
                        .asImageBitmap()
                    Image(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clip(CircleShape)
                            .clickable {
                                launcher.launch("image/*")
                            }
                            .size(100.dp),
                        bitmap = userImage,
                        contentDescription = null
                    )
                    OutlinedTextField(
                        modifier = Modifier.width(280.dp),
                        value = name,
                        onValueChange = { input ->
                            name = input.trim()
                        },
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
                            //TODO: create screen with country list
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
                                if (name.isEmpty() || surname.isEmpty() || email.isEmpty()
                                    || country.isEmpty() || city.isEmpty() || password.isEmpty()
                                ) {
                                    Toast.makeText(
                                        context,
                                        R.string.all_fields_must_be_filled,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                if (password.length < MIN_PASSWORD_LENGTH) {
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
                                val userRegister = UserRegister(
                                    name = name,
                                    surname = surname,
                                    email = email,
                                    country = country,
                                    city = city,
                                    password = password,
                                    image = String(
                                        Base64.encode(
                                            userImage.asAndroidBitmap().toByteArray(),
                                            Base64.DEFAULT
                                        )
                                    )
                                )
                                authViewModel.signUpUser(
                                    userRegister = userRegister,
                                    onSuccess = {
                                        navHostController.replaceTo(Root.Home.route)
                                    },
                                    onHttpError = { error ->
                                        when (error.code()) {
                                            409 -> {
                                                Toast.makeText(
                                                    context,
                                                    error.message,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                )
                            }
                        ) {
                            Text(text = stringResource(id = R.string.sign_up))
                        }
                    }
                }
            }
        }
    }
}
