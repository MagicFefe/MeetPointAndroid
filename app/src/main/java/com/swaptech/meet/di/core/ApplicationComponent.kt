package com.swaptech.meet.di.core

import com.swaptech.meet.presentation.MeetPointApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<MeetPointApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<MeetPointApplication>
}
