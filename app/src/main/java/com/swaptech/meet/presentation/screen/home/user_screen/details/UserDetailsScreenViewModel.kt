package com.swaptech.meet.presentation.screen.home.user_screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.map.MapPositionInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserDetailsScreenViewModel @Inject constructor(
    private val mapPositionInteractor: MapPositionInteractor
): ViewModel() {

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            mapPositionInteractor.deleteAll()
        }
    }
}
