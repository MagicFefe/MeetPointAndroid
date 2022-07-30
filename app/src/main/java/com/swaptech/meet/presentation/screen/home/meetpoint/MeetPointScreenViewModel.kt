package com.swaptech.meet.presentation.screen.home.meetpoint

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaptech.meet.domain.meet.model.CreateMeetPoint
import com.swaptech.meet.domain.meet.interactor.MeetPointInteractor
import com.swaptech.meet.domain.meet.model.DeleteMeetPoint
import com.swaptech.meet.domain.meet.model.MeetPointResponseDetails
import com.swaptech.meet.domain.meet.model.UpdateMeetPoint
import com.swaptech.meet.presentation.WORLD_LEVEL_ZOOM
import com.swaptech.meet.presentation.utils.bufferPrevious
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import retrofit2.HttpException
import javax.inject.Inject

class MeetPointScreenViewModel @Inject constructor(
    private val meetPointInteractor: MeetPointInteractor
) : ViewModel() {

    var firstLocationFixHappened by mutableStateOf(false)
        private set
    var meetPointMarker: Marker? by mutableStateOf(null)
        private set
    var screenState: MeetPointScreenState by mutableStateOf(MeetPointScreenState.Idle)
        private set
    var savedMapPosition: IGeoPoint by mutableStateOf(GeoPoint(0.0, 0.0))
        private set
    var savedMapZoomLevel: Double by mutableStateOf(WORLD_LEVEL_ZOOM)
        private set
    var clickedMeetPoint: MeetPointResponseDetails? by mutableStateOf(null)
        private set

    val meetPoints = meetPointInteractor.receiveMeetPoints()
        .onStart {
            meetPointInteractor.subscribe()
        }
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)
    val cachedMeetPoints = meetPoints
        .bufferPrevious()
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    fun onFirstFixLocation() {
        firstLocationFixHappened = true
    }

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

    fun hideMeetPointDetails() {
        screenState = MeetPointScreenState.Idle
    }

    fun saveMapPosition(point: IGeoPoint) {
        savedMapPosition = point
    }

    fun saveMapZoomLevel(zoomLevel: Double) {
        savedMapZoomLevel = zoomLevel
    }

    fun createMeetPoint(createMeetPoint: CreateMeetPoint, onHttpError: (HttpException) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                meetPointInteractor.createMeetPoint(createMeetPoint)
            } catch (httpException: HttpException) {
                viewModelScope.launch(Dispatchers.Main) {
                    onHttpError(httpException)
                }
            }
        }
    }

    fun updateMeetPoint(updateMeetPoint: UpdateMeetPoint) {
        viewModelScope.launch(Dispatchers.IO) {
            meetPointInteractor.updateMeetPoint(updateMeetPoint)
        }
    }

    fun getMeetPointById(meetPointId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            clickedMeetPoint = meetPointInteractor.getMeetPointById(meetPointId)
        }
    }

    fun deleteMeetPoint(deleteMeetPoint: DeleteMeetPoint) {
        viewModelScope.launch(Dispatchers.IO) {
            meetPointInteractor.deleteMeetPoint(deleteMeetPoint)
        }
    }
}
