package com.swaptech.meet.domain.meet.interactor

import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.model.MeetPointResponse
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import com.swaptech.meet.domain.meet.repository.MeetPointRepository
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

    suspend fun updateMeetPoint(updateMeetPoint: UpdateMeetPoint) {
        repository.updateMeetPoint(updateMeetPoint)
    }

    suspend fun deleteMeetPoint(meetPointId: String) {
        repository.deleteMeetPoint(meetPointId)
    }
}
