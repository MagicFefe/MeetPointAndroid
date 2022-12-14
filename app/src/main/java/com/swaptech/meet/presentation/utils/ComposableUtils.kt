package com.swaptech.meet.presentation.utils

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import com.swaptech.meet.presentation.utils.network_error_handling.Success

@Composable
fun <P, F: Any> FetchWithParam(
    param: P,
    action: suspend (P) -> NetworkResponse<F>,
    onLoading: @Composable () -> Unit,
    onCompletion: @Composable (F?) -> Unit
) {
    var fetched: NetworkResponse<F>? by remember(param) {
        mutableStateOf(null)
    }
    LaunchedEffect(param) {
        fetched = action(param)
    }
    when(fetched) {
        null -> {
            onLoading()
        }
        else -> {
            onCompletion((fetched!! as? Success)?.result)
        }
    }
}

@Composable
fun rememberMutableInteractionSource(): MutableInteractionSource = remember {
    MutableInteractionSource()
}
