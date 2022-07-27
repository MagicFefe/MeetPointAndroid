package com.swaptech.meet.presentation.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.bufferPrevious(count: Int = 1): Flow<T> = flow {
    val buffer = ArrayDeque<T>(count)
    collect { data ->
        if (buffer.size == count) {
            emit(buffer.removeFirst())
        }
        buffer.addLast(data)
    }
}
