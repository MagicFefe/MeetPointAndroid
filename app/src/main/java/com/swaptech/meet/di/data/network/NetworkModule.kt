package com.swaptech.meet.di.data.network

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swaptech.meet.data.interceptor.AuthorizationInterceptor
import com.swaptech.meet.data.ssl.MeetPointTrustManager
import com.swaptech.meet.presentation.BASE_URL
import com.swaptech.meet.presentation.MEET_POINTS_WS_URL
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponseCallAdapterFactory
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule {

    @Provides
    fun provideAuthorizationInterceptor(sharedPreferences: SharedPreferences): AuthorizationInterceptor =
        AuthorizationInterceptor(sharedPreferences)

    //TODO: TRUST ALL CERTIFICATES IS A BAD PRACTICE, REMOVE IT WHEN APP HAS BEEN RELEASED!!!
    @Provides
    fun provideMeetPointTrustManager(): X509TrustManager =
        MeetPointTrustManager()


    @Provides
    fun provideSSLContext(trustManager: X509TrustManager): SSLContext =
        SSLContext.getInstance("TLS")
            .also { sslContext ->
                sslContext.init(null, arrayOf(trustManager), SecureRandom())
            }

    @Provides
    fun provideSSLSocketFactory(sslContext: SSLContext): SSLSocketFactory =
        sslContext.socketFactory

    //TODO: REMOVE LOGS FROM PROD VERSION OF APP!!!
    @Provides
    fun provideOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor,
        sslSocketFactory: SSLSocketFactory,
        trustManager: X509TrustManager
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .sslSocketFactory(sslSocketFactory, trustManager)
        .hostnameVerifier { hostname, session -> true }
        .addInterceptor(authorizationInterceptor)
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().serializeNulls().create()

    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(NetworkResponseCallAdapterFactory())
            .build()

    @Provides
    fun provideScarletClient(
        okHttpClient: OkHttpClient
    ): Scarlet =
        Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(MEET_POINTS_WS_URL))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .build()
}
