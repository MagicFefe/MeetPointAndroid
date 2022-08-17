package com.swaptech.meet.presentation.screen.home.meetpoint

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.map.MapPosition
import com.swaptech.meet.domain.map.MapPositionInteractor
import com.swaptech.meet.domain.meet.interactor.MeetPointInteractor
import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.model.DeleteMeetPoint
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import com.swaptech.meet.presentation.WORLD_LEVEL_ZOOM
import com.swaptech.meet.presentation.utils.bufferPrevious
import com.swaptech.meet.presentation.utils.network_error_handling.onError
import com.swaptech.meet.presentation.utils.network_error_handling.onFail
import com.swaptech.meet.presentation.utils.network_error_handling.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

class MeetPointScreenViewModel @Inject constructor(
    private val meetPointInteractor: MeetPointInteractor,
    private val mapPositionInteractor: MapPositionInteractor
) : ViewModel() {

    var meetPointMarker: Marker? by mutableStateOf(null)
        private set
    var screenState: MeetPointScreenState by mutableStateOf(MeetPointScreenState.Idle)
        private set
    var savedMapCenter: IGeoPoint by mutableStateOf(GeoPoint(0.0, 0.0))
        private set
    var savedMapZoomLevel: Double by mutableStateOf(WORLD_LEVEL_ZOOM)
        private set
    var savedMapCenterOffsetX: Int by mutableStateOf(0)
        private set
    var savedMapCenterOffsetY: Int by mutableStateOf(0)
        private set
    val mapPositionDB = flow {
        emit(mapPositionInteractor.getMapCenter())
    }

    var clickedMeetPoint: MeetPointResponseDetails? by mutableStateOf(null)
        private set
            
    var meetPointCreatingInProgress by mutableStateOf(false)
        private set
    var meetPointByIdReceivingInProgress by mutableStateOf(false)
        private set
    var meetPointUpdatingInProgress by mutableStateOf(false)
        private set
    var meetPointByIdDeletingInProgress by mutableStateOf(false)
        private set

    val meetPoints = meetPointInteractor.receiveMeetPoints()
        .onStart {
            meetPointInteractor.subscribe()
        }
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)
    val cachedMeetPoints = meetPoints
        .bufferPrevious()
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)


    fun addMeetPointMarker(marker: Marker, map: MapView) {
        meetPointMarker = marker
        screenState = MeetPointScreenState.CreateMeetPoint(map)
    }

    fun removeMeetPointMarker() {
        meetPointMarker = null
        screenState = MeetPointScreenState.Idle
    }

    fun showUpdateMeetPointCard() {
        screenState = MeetPointScreenState.UpdateMeetPoint
    }

    fun showMeetPointDetails(meetPointId: String) {
        screenState = MeetPointScreenState.ShowMeetPointDetails(meetPointId)
    }

    fun hideMeetPointCard() {
        screenState = MeetPointScreenState.Idle
    }

    fun saveMapPosition(point: IGeoPoint) {
        savedMapCenter = point
    }

    fun saveMapZoomLevel(zoomLevel: Double) {
        savedMapZoomLevel = zoomLevel
    }

    fun saveCenterOffset(x: Int, y: Int) {
        savedMapCenterOffsetX = x
        savedMapCenterOffsetY = y
    }

    fun saveCurrentLocation(latitude: Double, longitude: Double) {
        savedMapCenter = GeoPoint(latitude, longitude)
    }

    fun createMeetPoint(
        createMeetPoint: CreateMeetPoint,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        meetPointCreatingInProgress = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = meetPointInteractor.createMeetPoint(createMeetPoint)
            viewModelScope.launch(Dispatchers.Main) {
                response
                    .onFail(onFail)
                    .onError(onError)
                meetPointCreatingInProgress = false
                removeMeetPointMarker()
            }
        }
    }

    fun updateMeetPoint(
        updateMeetPoint: UpdateMeetPoint,
        onSuccess: (Unit) -> Unit,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        meetPointUpdatingInProgress = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = meetPointInteractor.updateMeetPoint(updateMeetPoint)
            viewModelScope.launch(Dispatchers.Main) {
                response
                    .onSuccess(onSuccess)
                    .onFail(onFail)
                    .onError(onError)
                meetPointUpdatingInProgress = false
            }
        }
    }

    fun getMeetPointById(
        meetPointId: String,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        meetPointByIdReceivingInProgress = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = meetPointInteractor.getMeetPointById(meetPointId)
            viewModelScope.launch(Dispatchers.Main) {
                response
                    .onSuccess {
                        clickedMeetPoint = it
                    }
                    .onFail(onFail)
                    .onError(onError)
                meetPointByIdReceivingInProgress = false
            }
        }
    }

    fun deleteMeetPoint(
        deleteMeetPoint: DeleteMeetPoint,
        onSuccess: (Unit) -> Unit,
        onFail: (Int, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        meetPointByIdDeletingInProgress = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = meetPointInteractor.deleteMeetPoint(deleteMeetPoint)
            viewModelScope.launch(Dispatchers.Main) {
                response
                    .onSuccess(onSuccess)
                    .onFail(onFail)
                    .onError(onError)
                meetPointByIdDeletingInProgress = false
            }
        }
    }

    fun updateMapPositionDB() {
        viewModelScope.launch(Dispatchers.IO) {
            mapPositionInteractor.save(
                MapPosition(
                    latitude = savedMapCenter.latitude,
                    longitude = savedMapCenter.longitude,
                    mapCenterOffsetX = savedMapCenterOffsetX,
                    mapCenterOffsetY = savedMapCenterOffsetY,
                    zoomLevel = savedMapZoomLevel
                )
            )
        }
    }
}
