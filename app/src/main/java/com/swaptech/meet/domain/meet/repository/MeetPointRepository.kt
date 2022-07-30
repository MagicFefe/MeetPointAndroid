package com.swaptech.meet.domain.meet.repository

import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.model.MeetPointResponse
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import kotlinx.coroutines.flow.Flow

interface MeetPointRepository {
    fun subscribe()
    fun receiveMeetPoints(): Flow<List<MeetPointResponse>>
    suspend fun getMeetPointById(meetPointId: String): MeetPointResponseDetails
    suspend fun createMeetPoint(createMeetPoint: CreateMeetPoint)
    suspend fun updateMeetPoint(updateMeetPoint: UpdateMeetPoint)
    suspend fun deleteMeetPoint(meetPointId: String)
}
