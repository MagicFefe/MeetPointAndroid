package com.swaptech.meet.di.presentation

import com.swaptech.meet.di.presentation.activity.ActivityModule
import com.swaptech.meet.di.presentation.viewmodel.ViewModelModule
import dagger.Module

@Module(
    includes = [
        ActivityModule::class, ViewModelModule::class
    ]
)
class PresentationModule
