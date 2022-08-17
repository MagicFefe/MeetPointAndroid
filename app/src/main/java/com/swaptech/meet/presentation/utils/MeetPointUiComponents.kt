package com.swaptech.meet.presentation.utils

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swaptech.meet.R
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.presentation.utils.country_chooser.CountryChooser
import com.swaptech.meet.presentation.utils.country_chooser.CountryChooserState
import com.swaptech.meet.presentation.utils.country_chooser.rememberCountryChooserState
import androidx.compose.ui.graphics.Color as ColorCompose

@Composable
fun MeetPointCreationUpdateCard(
    cardTitle: String,
    meetPointName: String,
    onMeetPointNameChange: (String) -> Unit,
    meetPointDescription: String,
    onMeetPointDescriptionChange: (String) -> Unit,
    onCloseButtonCLick: () -> Unit,
    doneButton: (@Composable () -> Unit)? = null,
    onDoneButtonClick: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 3.dp, top = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCloseButtonCLick
                ) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                }
                Text(
                    text = cardTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                if (doneButton != null) {
                    Column(
                        modifier = Modifier.padding(end = 13.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        doneButton()
                    }
                } else {
                    IconButton(
                        modifier = Modifier.padding(end = 3.dp),
                        onClick = onDoneButtonClick
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Done,
                            contentDescription = null
                        )
                    }
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp),
                value = meetPointName,
                onValueChange = onMeetPointNameChange,
                label = {
                    Text(text = stringResource(id = R.string.meet_point_name))
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp),
                value = meetPointDescription,
                onValueChange = onMeetPointDescriptionChange,
                label = {
                    Text(text = stringResource(id = R.string.meet_point_description))
                }
            )
        }
    }
}

@Preview
@Composable
fun MeetPointCreationMin_Preview() {
    val (name, onNameChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (description, onDescriptionChange) = rememberSaveable {
        mutableStateOf("")
    }
    MeetPointCreationUpdateCard(
        "",
        name,
        onNameChange,
        description,
        onDescriptionChange,
        {},
        doneButton = { CardProgressIndicator() },
        onDoneButtonClick = {}
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MeetPointDetails(
    modifier: Modifier = Modifier,
    meetPoint: MeetPointResponseDetails?,
    isLoading: Boolean,
    onCloseButtonCLick: () -> Unit,
    onAuthorClick: () -> Unit,
    actionButtons: (@Composable RowScope.() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 3.dp, top = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCloseButtonCLick
                ) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                }
                Text(
                    text = stringResource(id = R.string.meet_point_details),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                if (meetPoint != null || !isLoading) {
                    actionButtons?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            content = actionButtons
                        )
                    }
                }
            }
            AnimatedContent(targetState = meetPoint == null || isLoading) {
                if (meetPoint == null || isLoading) {
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column {
                        Column(
                            modifier = Modifier.padding(start = 17.dp, end = 17.dp)
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.meet_point_name_with_params,
                                    meetPoint.meetName
                                ),
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.meet_point_description_with_params,
                                    meetPoint.meetDescription
                                ),
                                style = MaterialTheme.typography.subtitle2
                            )
                            Text(
                                modifier = Modifier.padding(bottom = 5.dp),
                                text = stringResource(
                                    id = R.string.meet_point_created_at,
                                    meetPoint.createdAt
                                ),
                                style = MaterialTheme.typography.subtitle2
                            )
                        }
                        Column(
                            modifier = Modifier
                                .clickable(
                                    onClick = onAuthorClick
                                )
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val decodedProfileImage = meetPoint.authorImage.toByteArray()
                            UserHeader(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .size(20.dp),
                                userName = meetPoint.authorName,
                                userSurname = meetPoint.authorSurname,
                                profileImage = decodedProfileImage
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MeetPointDetails_Preview() {
    var description = ""
    for (i in 0..8) {
        description += description.ifEmpty {
            "a"
        }
    }
    MeetPointDetails(
        meetPoint = MeetPointResponseDetails(
            id = "",
            authorId = "",
            meetName = "Talk with new friends",
            meetDescription = description,
            latitude = 2.0,
            longitude = 2.0,
            createdAt = "23.07.2022",
            authorName = "Ivan",
            authorSurname = "Ivanov",
            authorImage = ""
        ),
        isLoading = true,
        onCloseButtonCLick = {},
        onAuthorClick = {}
    ) {
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
        }
        IconButton(onClick = { }) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
        }
    }
}


@Composable
fun CardProgressIndicator(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colors.onSurface
) {
    CircularProgressIndicator(
        modifier = modifier.size(20.dp),
        color = color,
        strokeWidth = 2.dp
    )
}

@Composable
fun UserImage(
    modifier: Modifier,
    userImage: ByteArray?
) {
    if (userImage == null || userImage.isEmpty()) {
        Image(
            modifier = modifier.clip(CircleShape),
            painter = painterResource(R.drawable.user_image_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            modifier = modifier.clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(userImage)
                .placeholder(R.drawable.user_image_placeholder)
                .build(),
            contentDescription = null
        )
    }
}

@Composable
fun UserHeader(
    modifier: Modifier,
    userName: String,
    userSurname: String,
    profileImage: ByteArray?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserImage(
            modifier = modifier,
            userImage = profileImage
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = userName,
            fontSize = 16.sp
        )
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = userSurname,
            fontSize = 16.sp
        )
    }
}

@Composable
fun VerticalScrollableContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    stickyBottomContent: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState),
            content = content
        )
        stickyBottomContent()
    }
}

@Composable
fun OutlinedClickableTextField(
    modifier: Modifier = Modifier,
    value: String,
    onClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = rememberMutableInteractionSource()
    val clicked by interactionSource.collectIsPressedAsState()
    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        readOnly = true,
        value = value,
        onValueChange = {
        },
        placeholder = {
            Text(text = stringResource(id = R.string.select_country))
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.Public,
                contentDescription = null
            )
        },
        interactionSource = interactionSource.also {
            if (clicked) {
                onClick()
                focusRequester.requestFocus()
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpdateSignUpUserForm(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    surname: String,
    onSurnameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    country: String,
    onCountryClick: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    gender: String,
    onGenderChooserItemClick: (String) -> Unit,
    about: String,
    onAboutChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    image: ByteArray? = null,
    onImageChooseResult: (ByteArray?) -> Unit,
    onCloseButtonClick: () -> Unit,
    finishButton: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val countryChooserState = rememberCountryChooserState(CountryChooserState.Value.Hidden)
    val scrollState = rememberScrollState()
    var userImage by rememberSaveable {
        mutableStateOf(image)
    }
    var genderChooserExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val unprocessedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                userImage = unprocessedBitmap.resizeToProfileImage().toByteArray()
                onImageChooseResult(userImage)
            }
        }
    )
    if (countryChooserState.isHidden) {
        VerticalScrollableContent(
            modifier = modifier,
            scrollState = scrollState,
            stickyBottomContent = finishButton
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
                onClick = onCloseButtonClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = surname,
                    onValueChange = onSurnameChange,
                    label = {
                        Text(text = stringResource(id = R.string.surname))
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = email,
                    onValueChange = onEmailChange,
                    label = {
                        Text(text = stringResource(id = R.string.email))
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedClickableTextField(
                    value = country,
                    onClick = {
                        countryChooserState.show()
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = city,
                    onValueChange = onCityChange,
                    label = {
                        Text(text = stringResource(id = R.string.city))
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                ExposedDropdownMenuBox(
                    expanded = genderChooserExpanded,
                    onExpandedChange = {
                        genderChooserExpanded = !genderChooserExpanded
                    }
                ) {
                    CompositionLocalProvider(
                        LocalTextToolbar provides EmptyTextToolbar
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = onGenderChooserItemClick,
                            label = {
                                Text(text = stringResource(id = R.string.choose_your_gender))
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = genderChooserExpanded
                                )
                            },
                            readOnly = true
                        )
                    }
                    ExposedDropdownMenu(
                        expanded = genderChooserExpanded,
                        onDismissRequest = {
                            genderChooserExpanded = false
                        }
                    ) {
                        val genders = stringArrayResource(id = R.array.genders)
                        genders.forEach { gender ->
                            DropdownMenuItem(
                                onClick = {
                                    onGenderChooserItemClick(gender)
                                    genderChooserExpanded = false
                                }
                            ) {
                                Text(text = gender)
                            }
                        }
                    }
                }
                CompositionLocalProvider(
                    LocalTextToolbar provides EmptyTextToolbar
                ) {
                    OutlinedTextField(
                        modifier = Modifier.width(280.dp),
                        value = date,
                        onValueChange = onDateChange,
                        label = {
                            Text(text = stringResource(id = R.string.date_of_birth))
                        },
                        placeholder = {
                            Text(text = "dd-mm-yyyy")
                        },
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Number
                        ),
                        visualTransformation = DateVisualTransformation()
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.width(280.dp),
                    value = about,
                    onValueChange = onAboutChange,
                    label = {
                        Text(text = stringResource(id = R.string.about_placeholder))
                    },
                    maxLines = 5
                )
                content()
            }
        }
    } else {
        CountryChooser(
            onBackButtonClick = {
                countryChooserState.hide()
            },
            onCountryClick = { selected ->
                onCountryClick(selected)
                countryChooserState.hide()
            }
        )
    }
}

@Composable
fun Separator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(ColorCompose.LightGray)
    )
}

@Composable
fun LoadingPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}
