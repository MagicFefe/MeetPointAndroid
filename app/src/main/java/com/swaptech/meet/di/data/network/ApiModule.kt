package com.swaptech.meet.di.data.network

import com.swaptech.meet.data.auth.AuthApi
import com.swaptech.meet.data.feedback.FeedbackApi
import com.swaptech.meet.data.meetpoint.MeetPointApi
import com.swaptech.meet.data.user.UserApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiModule {

    @Provides
    fun provideMeetPointApi(
        retrofitClient: Retrofit
    ): MeetPointApi =
        retrofitClient.create(MeetPointApi::class.java)

    @Provides
    fun provideUserApi(
        retrofitClient: Retrofit
    ): UserApi =
        retrofitClient.create(UserApi::class.java)

    @Provides
    fun provideAuthApi(
        retrofitClient: Retrofit
    ): AuthApi =
        retrofitClient.create(AuthApi::class.java)

    @Provides
    fun feedbackApi(
        retrofitClient: Retrofit
    ): FeedbackApi =
        retrofitClient.create(FeedbackApi::class.java)
}
