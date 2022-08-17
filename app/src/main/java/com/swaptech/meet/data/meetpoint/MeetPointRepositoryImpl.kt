package com.swaptech.meet.data.meetpoint

import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.model.DeleteMeetPoint
import com.swaptech.meet.domain.meet.model.MeetPointResponse
import com.swaptech.meet.domain.meet.repository.MeetPointRepository
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class MeetPointRepositoryImpl @Inject constructor(
    private val meetPointService: MeetPointService,
    private val meetPointApi: MeetPointApi
) : MeetPointRepository {

    override fun subscribe() {
        meetPointService.subscribe("text")
    }

    override fun receiveMeetPoints(): Flow<List<MeetPointResponse>> =
        meetPointService.receiveMeetPoints().receiveAsFlow()


    override suspend fun createMeetPoint(createMeetPoint: CreateMeetPoint): NetworkResponse<Unit> =
        meetPointApi.createMeetPoint(createMeetPoint)


    override suspend fun getMeetPointById(meetPointId: String): NetworkResponse<MeetPointResponseDetails> =
        meetPointApi.getMeetPointById(meetPointId)

    override suspend fun updateMeetPoint(updateMeetPoint: UpdateMeetPoint): NetworkResponse<Unit> =
        meetPointApi.updateMeetPoint(updateMeetPoint)


    override suspend fun deleteMeetPoint(deleteMeetPoint: DeleteMeetPoint): NetworkResponse<Unit> =
        meetPointApi.deleteMeetPoint(deleteMeetPoint)
}
