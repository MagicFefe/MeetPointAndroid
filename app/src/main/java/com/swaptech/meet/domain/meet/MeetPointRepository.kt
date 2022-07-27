package com.swaptech.meet.domain.meet

import kotlinx.coroutines.flow.Flow

interface MeetPointRepository {
    fun subscribe()
    fun receiveMeetPoints(): Flow<List<MeetPointResponse>>
    suspend fun getMeetPointById(meetPointId: String): MeetPointResponseDetails
    suspend fun createMeetPoint(createMeetPoint: CreateMeetPoint)
    suspend fun deleteMeetPoint(meetPointId: String)
}
