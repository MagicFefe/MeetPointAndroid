package com.swaptech.meet.di.data.db

import android.content.Context
import androidx.room.Room
import com.swaptech.meet.data.user.UserDatabase
import com.swaptech.meet.presentation.USER_DB_NAME
import dagger.Module
import dagger.Provides

@Module
class DbModule {

    @Provides
    fun provideUserDatabase(context: Context): UserDatabase =
        Room.databaseBuilder(context, UserDatabase::class.java, USER_DB_NAME).build()
}
