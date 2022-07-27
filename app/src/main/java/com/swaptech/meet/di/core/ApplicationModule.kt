package com.swaptech.meet.di.core

import android.content.Context
import com.swaptech.meet.di.data.DataModule
import com.swaptech.meet.di.presentation.PresentationModule
import com.swaptech.meet.presentation.MeetPointApplication
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        PresentationModule::class, DataModule::class
    ]
)
class ApplicationModule {
    @Provides
    fun provideContext(meetPointApplication: MeetPointApplication): Context =
        meetPointApplication.applicationContext
}
