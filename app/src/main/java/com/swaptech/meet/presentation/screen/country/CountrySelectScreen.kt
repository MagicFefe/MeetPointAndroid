package com.swaptech.meet.presentation.screen.country

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swaptech.meet.R
import java.util.*

//TODO
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountrySelectScreen(
    onCountryClick: (String) -> Unit
) {
    val countries by rememberSaveable {
        mutableStateOf(
            Locale.getAvailableLocales()
                .map { it.displayCountry }
                .filterNot { it.isEmpty() }
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.select_country)
            )
        }
        items(countries) { country ->
            CountryItem(
                country = country,
                onItemClick = onCountryClick
            )
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
            .clickable {
                onItemClick(country)
            }
            .fillMaxWidth()
    ) {
       Text(
           modifier = Modifier.padding(10.dp),
           text = country
       )
    }
}
