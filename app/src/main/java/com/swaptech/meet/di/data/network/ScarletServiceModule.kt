package com.swaptech.meet.di.data.network

import com.swaptech.meet.data.meetpoint.MeetPointService
import com.tinder.scarlet.Scarlet
import dagger.Module
import dagger.Provides

@Module
class ScarletServiceModule {

    @Provides
    fun provideMeetPointService(
        scarletClient: Scarlet
    ): MeetPointService =
        scarletClient.create(MeetPointService::class.java)
}
