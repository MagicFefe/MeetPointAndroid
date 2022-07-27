package com.swaptech.meet.di.data.db

import com.swaptech.meet.data.user.UserDao
import com.swaptech.meet.data.user.UserDatabase
import dagger.Module
import dagger.Provides

@Module
class DaoModule {

    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao =
        userDatabase.getUserDao()
}
