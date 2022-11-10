package com.company.activityart.presentation.saveArtScreen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed interface SaveArtViewEvent : ViewEvent {
    object ClickedNavigateUp : SaveArtViewEvent
}

sealed interface SaveArtViewState : ViewState {

}

