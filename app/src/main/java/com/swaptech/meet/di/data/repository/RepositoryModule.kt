package com.swaptech.meet.di.data.repository

import com.swaptech.meet.data.auth.AuthRepositoryImpl
import com.swaptech.meet.data.feedback.FeedbackRepositoryImpl
import com.swaptech.meet.data.meetpoint.MeetPointRepositoryImpl
import com.swaptech.meet.data.user.UserRepositoryImpl
import com.swaptech.meet.domain.auth.repository.AuthRepository
import com.swaptech.meet.domain.feedback.repository.FeedbackRepository
import com.swaptech.meet.domain.meet.repository.MeetPointRepository
import com.swaptech.meet.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindMeetPointRepository(repository: MeetPointRepositoryImpl): MeetPointRepository

    @Binds
    fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    fun bindAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindFeedbackRepository(repository: FeedbackRepositoryImpl): FeedbackRepository
}
