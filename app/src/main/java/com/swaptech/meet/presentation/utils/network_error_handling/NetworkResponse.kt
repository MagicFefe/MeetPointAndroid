package com.swaptech.meet.presentation.utils.network_error_handling

sealed interface NetworkResponse<T: Any>

class Success<T: Any>(val result: T): NetworkResponse<T>
class Fail<T: Any>(val code: Int, val message: String): NetworkResponse<T>
class Error<T: Any>(val throwable: Throwable): NetworkResponse<T>
