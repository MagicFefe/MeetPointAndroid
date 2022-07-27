package com.swaptech.meet.presentation.screen.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import java.util.*
import javax.inject.Inject

class SignUpViewModel @Inject constructor() : ViewModel() {
    val countries = flow {
        val availableCountries = Locale.getAvailableLocales()
            .map { it.displayCountry }
            .filter { it.isNotEmpty() }
        emit(availableCountries)
    }
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
}
