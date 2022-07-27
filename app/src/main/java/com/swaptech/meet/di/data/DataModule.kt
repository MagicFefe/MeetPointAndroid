package com.swaptech.meet.di.data

import com.swaptech.meet.di.data.db.DaoModule
import com.swaptech.meet.di.data.db.DbModule
import com.swaptech.meet.di.data.network.ApiModule
import com.swaptech.meet.di.data.network.NetworkModule
import com.swaptech.meet.di.data.network.ScarletServiceModule
import com.swaptech.meet.di.data.repository.RepositoryModule
import com.swaptech.meet.di.data.sharedprefs.SharedPreferencesModule
import dagger.Module

@Module(
    includes = [
        NetworkModule::class, ApiModule::class, ScarletServiceModule::class,
        SharedPreferencesModule::class, DaoModule::class, DbModule::class,
        RepositoryModule::class
    ]
)
class DataModule
