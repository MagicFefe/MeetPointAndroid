package com.swaptech.meet.presentation.utils

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun <F, P> FetchWithParam(
    param: P,
    action: suspend (P) -> F,
    onCompletion: @Composable (F) -> Unit
) {
    var fetched: F? by remember(param) {
        mutableStateOf(null)
    }
    LaunchedEffect(param) {
        fetched = action(param)
    }
    fetched?.let {
        onCompletion(it)
    }
}

@Composable
fun rememberMutableInteractionSource(): MutableInteractionSource = remember {
    MutableInteractionSource()
}
