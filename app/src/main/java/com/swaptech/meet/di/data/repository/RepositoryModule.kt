package com.swaptech.meet.di.data.repository

import com.swaptech.meet.data.meetpoint.MeetPointRepositoryImpl
import com.swaptech.meet.data.user.UserRepositoryImpl
import com.swaptech.meet.domain.meet.MeetPointRepository
import com.swaptech.meet.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindMeetPointRepositoryModule(repository: MeetPointRepositoryImpl): MeetPointRepository

    @Binds
    fun bindUserRepositoryModule(repository: UserRepositoryImpl): UserRepository
}
