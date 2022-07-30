package com.swaptech.meet.data.meetpoint

import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MeetPointApi {
    @POST("meet")
    suspend fun createMeetPoint(@Body meetPoint: CreateMeetPoint)

    @GET("meet/{meet_point_id}")
    suspend fun getMeetPointById(@Path("meet_point_id") meetPointId: String): MeetPointResponseDetails

    @DELETE("meet/{meet_point_id}")
    suspend fun deleteMeetPoint(@Path("meet_point_id") meetPointId: String)

    @PUT("meet")
    suspend fun updateMeetPoint(@Body updateMeetPoint: UpdateMeetPoint)
}
