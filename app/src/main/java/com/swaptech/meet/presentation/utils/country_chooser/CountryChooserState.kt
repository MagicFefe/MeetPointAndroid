package com.swaptech.meet.presentation.utils.country_chooser

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
class CountryChooserState(
    initialValue: Value
) {

    enum class Value {
        Shown,
        Hidden
    }

    var currentValue: Value by mutableStateOf(initialValue)
        private set

    val isHidden: Boolean
        get() = currentValue == Value.Hidden

    fun show() {
        currentValue = Value.Shown
    }

    fun hide() {
        currentValue = Value.Hidden
    }

    companion object {
        val Saver: Saver<CountryChooserState, *> = Saver(
            save = { it.currentValue },
            restore = {
                CountryChooserState(it)
            }
        )
    }
}

@Composable
fun rememberCountryChooserState(initial: CountryChooserState.Value) =
    rememberSaveable(
        saver = CountryChooserState.Saver
    ) {
        CountryChooserState(initial)
    }
