package com.swaptech.meet.presentation.utils.network_error_handling

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkResponseCall<T : Any>(
    private val call: Call<T>
) : Call<NetworkResponse<T>> {

    override fun clone(): Call<NetworkResponse<T>> =
        NetworkResponseCall(call.clone())

    override fun execute(): Response<NetworkResponse<T>> =
        throw NotImplementedError()


    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val networkResponse = handleRequest { response }
                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(networkResponse)
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResponse = Error<T>(t)
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()
}
