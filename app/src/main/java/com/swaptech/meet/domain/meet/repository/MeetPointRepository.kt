package com.swaptech.meet.domain.meet.repository

import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.model.DeleteMeetPoint
import com.swaptech.meet.domain.meet.model.MeetPointResponse
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface MeetPointRepository {
    fun subscribe()
    fun receiveMeetPoints(): Flow<List<MeetPointResponse>>
    suspend fun getMeetPointById(meetPointId: String): NetworkResponse<MeetPointResponseDetails>
    suspend fun createMeetPoint(createMeetPoint: CreateMeetPoint): NetworkResponse<Unit>
    suspend fun updateMeetPoint(updateMeetPoint: UpdateMeetPoint): NetworkResponse<Unit>
    suspend fun deleteMeetPoint(deleteMeetPoint: DeleteMeetPoint): NetworkResponse<Unit>
}
