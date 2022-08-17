package com.swaptech.meet.di.data.db

import android.content.Context
import androidx.room.Room
import com.swaptech.meet.data.user.Database
import com.swaptech.meet.presentation.DB_NAME
import dagger.Module
import dagger.Provides

@Module
class DbModule {

    @Provides
    fun provideDatabase(context: Context): Database =
        Room.databaseBuilder(context, Database::class.java, DB_NAME).build()
}
