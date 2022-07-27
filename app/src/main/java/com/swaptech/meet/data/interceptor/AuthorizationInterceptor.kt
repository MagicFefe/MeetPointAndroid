package com.swaptech.meet.data.interceptor

import android.content.SharedPreferences
import android.util.Log
import com.swaptech.meet.presentation.AUTH_TOKEN_KEY
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val sharedPreferences: SharedPreferences
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString(AUTH_TOKEN_KEY, "")
        val originalRequest = chain.request()
        Log.d("HUIHUI", "token is - ${token}")
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", token ?: "")
            .build()
        return chain.proceed(newRequest)
    }
}
