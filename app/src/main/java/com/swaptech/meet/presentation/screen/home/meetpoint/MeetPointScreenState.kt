package com.swaptech.meet.presentation.screen.home.meetpoint

import org.osmdroid.views.MapView

sealed class MeetPointScreenState {
    object Idle : MeetPointScreenState()
    class CreateMeetPoint(val map: MapView) : MeetPointScreenState()
    class ShowMeetPointDetails(val meetPointId: String) : MeetPointScreenState()
    object UpdateMeetPoint: MeetPointScreenState()
}
