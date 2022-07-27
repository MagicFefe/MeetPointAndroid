package com.swaptech.meet.presentation

import com.swaptech.meet.di.core.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MeetPointApplication : DaggerApplication() {
    public override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.factory().create(this)
}
