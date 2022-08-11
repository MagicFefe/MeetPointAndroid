package com.swaptech.meet.presentation.utils.network_error_handling

import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

fun <T: Any> handleRequest(block: () -> Response<T>) =
    try {
        val response = block()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Success(body)
        } else {
            Fail(
                code = response.code(),
                message = response.message()
            )
        }
    } catch (httpException: HttpException) {
        Fail(
            code = httpException.code(),
            message = httpException.message()
        )
    } catch (throwable: Throwable) {
        Error(throwable)
    }

suspend fun <T: Any> NetworkResponse<T>.onSuccess(
    block: suspend (T) -> Unit
): NetworkResponse<T> = apply {
    if (this is Success<T>) {
        block(result)
    }
}

suspend fun <T: Any> NetworkResponse<T>.onFail(
    block: suspend (Int, String) -> Unit
): NetworkResponse<T> = apply {
    if (this is Fail<T>) {
        block(code, message)
    }
}

suspend fun <T: Any> NetworkResponse<T>.onError(
    block: suspend (Throwable) -> Unit
): NetworkResponse<T> = apply {
    if (this is Error<T>) {
        block(throwable)
    }
}

fun handleError(
    throwable: Throwable,
    onConnectionFault: () -> Unit,
    onSocketTimeout: () -> Unit,
    block: () -> Unit = {}
) {
    when(throwable) {
        is ConnectException -> onConnectionFault()
        is SocketTimeoutException -> onSocketTimeout()
        else -> block()
    }
}
