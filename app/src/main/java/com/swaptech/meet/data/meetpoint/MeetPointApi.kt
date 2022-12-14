package com.swaptech.meet.data.meetpoint

import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.model.DeleteMeetPoint
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import com.swaptech.meet.presentation.utils.network_error_handling.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MeetPointApi {
    @POST("meet")
    suspend fun createMeetPoint(@Body meetPoint: CreateMeetPoint): NetworkResponse<Unit>

    @GET("meet/{meet_point_id}")
    suspend fun getMeetPointById(@Path("meet_point_id") meetPointId: String): NetworkResponse<MeetPointResponseDetails>

    @HTTP(method = "DELETE", path = "meet", hasBody = true)
    suspend fun deleteMeetPoint(@Body deleteMeetPoint: DeleteMeetPoint): NetworkResponse<Unit>

    @PUT("meet")
    suspend fun updateMeetPoint(@Body updateMeetPoint: UpdateMeetPoint): NetworkResponse<Unit>
}
