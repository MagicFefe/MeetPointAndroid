package com.swaptech.meet.di.data.db

import com.swaptech.meet.data.map.MapPositionDao
import com.swaptech.meet.data.user.UserDao
import com.swaptech.meet.data.user.Database
import dagger.Module
import dagger.Provides

@Module
class DaoModule {

    @Provides
    fun provideUserDao(database: Database): UserDao =
        database.getUserDao()

    @Provides
    fun provideMapPositionDao(database: Database): MapPositionDao =
        database.getMapPositionDao()
}
