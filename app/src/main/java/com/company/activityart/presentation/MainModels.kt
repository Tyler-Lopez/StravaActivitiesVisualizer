package com.company.activityart.presentation

import android.graphics.Bitmap
import android.net.Uri
import com.company.activityart.architecture.Destination
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed interface MainViewState : ViewState {
    object LoadingAuthentication : MainViewState
    object Unauthenticated : MainViewState
    data class Authenticated(
        val athleteId: Long,
        val accessToken: String
    ) : MainViewState
}

sealed interface MainViewEvent : ViewEvent {
    data class LoadAuthentication(val uri: Uri?) : MainViewEvent
}

sealed interface MainDestination : Destination {
    object ConnectWithStrava : MainDestination
    object NavigateAbout : MainDestination
    object NavigateLogin : MainDestination
    data class NavigateLoadActivities(
        val athleteId: String,
        val accessToken: String
    ) : MainDestination

    data class NavigateEditArt(val fromLoad: Boolean = true) : MainDestination
    data class NavigateSaveArt(
        val activityTypes: List<String>,
        val colorActivities: String,
        val colorBackground: String,
        val filterBeforeMs: Long,
        val filterAfterMs: Long,
        val sizeHeight: Int,
        val sizeWidth: Int,
        val strokeWidth: Int
    ) : MainDestination

    object NavigateUp : MainDestination
}