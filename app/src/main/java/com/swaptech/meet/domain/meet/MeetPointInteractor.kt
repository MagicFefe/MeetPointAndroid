package com.swaptech.meet.domain.meet

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeetPointInteractor @Inject constructor(private val repository: MeetPointRepository) {
    fun subscribe() = repository.subscribe()

    fun receiveMeetPoints(): Flow<List<MeetPointResponse>> =
        repository.receiveMeetPoints()

    suspend fun createMeetPoint(createMeetPoint: CreateMeetPoint) {
        repository.createMeetPoint(createMeetPoint)
    }

    suspend fun getMeetPointById(meetPointId: String): MeetPointResponseDetails =
        repository.getMeetPointById(meetPointId)

    suspend fun deleteMeetPoint(meetPointId: String) {
        repository.deleteMeetPoint(meetPointId)
    }
}
