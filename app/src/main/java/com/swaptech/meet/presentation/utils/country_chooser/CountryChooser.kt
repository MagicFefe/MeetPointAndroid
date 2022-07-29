package com.swaptech.meet.presentation.utils.country_chooser

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swaptech.meet.R
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountryChooser(
    onBackButtonClick: () -> Unit,
    onCountryClick: (String) -> Unit
) {
    var searchedCountry by rememberSaveable {
        mutableStateOf("")
    }
    val countries by rememberSaveable(searchedCountry) {
        mutableStateOf(
            Locale.getAvailableLocales()
                .map { it.displayCountry }
                .filterNot { it.isEmpty() }
                .toSet()
                .filter { country ->
                    country.lowercase()
                        .contains(
                            searchedCountry.lowercase()
                        )
                }
        )
    }
    Surface {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                Surface {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackButtonClick
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = searchedCountry,
                            onValueChange = { input ->
                                searchedCountry = input
                            },
                            placeholder = {
                                Text(text = stringResource(R.string.enter_country_name))
                            },
                            singleLine = true,
                            maxLines = 1,
                            trailingIcon = {
                                if (searchedCountry.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            searchedCountry = ""
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            shape = RectangleShape,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.surface,
                                focusedIndicatorColor = MaterialTheme.colors.surface,
                                unfocusedIndicatorColor = MaterialTheme.colors.surface
                            )
                        )
                    }
                }
            }
            items(countries) { country ->
                CountryItem(
                    country = country,
                    onItemClick = onCountryClick
                )
            }
        }
    }
}

@Composable
fun CountryItem(
    country: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onItemClick(country)
                },
                indication = rememberRipple(),
            )
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = country
        )
    }
}
