package com.swaptech.meet.data.meetpoint

import com.swaptech.meet.domain.meet.CreateMeetPoint
import com.swaptech.meet.domain.meet.MeetPointResponse
import com.swaptech.meet.domain.meet.MeetPointRepository
import com.swaptech.meet.domain.meet.MeetPointResponseDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class MeetPointRepositoryImpl @Inject constructor(
    private val meetPointService: MeetPointService,
    private val meetPointApi: MeetPointApi,
) : MeetPointRepository {
    override fun subscribe() {
        meetPointService.subscribe("text")
    }

    override fun receiveMeetPoints(): Flow<List<MeetPointResponse>> =
        meetPointService.receiveMeetPoints().receiveAsFlow()


    override suspend fun createMeetPoint(createMeetPoint: CreateMeetPoint) {
        meetPointApi.createMeetPoint(createMeetPoint)
    }

    override suspend fun getMeetPointById(meetPointId: String): MeetPointResponseDetails =
        meetPointApi.getMeetPointById(meetPointId)

    override suspend fun deleteMeetPoint(meetPointId: String) {
        meetPointApi.deleteMeetPoint(meetPointId)
    }
}
