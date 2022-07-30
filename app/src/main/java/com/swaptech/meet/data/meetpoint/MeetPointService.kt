package com.swaptech.meet.data.meetpoint

import com.swaptech.meet.domain.meet.model.MeetPointResponse
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel


interface MeetPointService {
    @Send
    fun subscribe(text: String)

    @Receive
    fun receiveMeetPoints(): ReceiveChannel<List<MeetPointResponse>>
}
