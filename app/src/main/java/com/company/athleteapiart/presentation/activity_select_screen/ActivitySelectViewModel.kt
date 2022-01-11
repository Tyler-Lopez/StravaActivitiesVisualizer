package com.company.athleteapiart.presentation.activity_select_screen

import android.content.AbstractThreadedSyncAdapter
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.data.remote.responses.Athlete
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.Oauth2
import com.company.athleteapiart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivitySelectViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    init {
        if (AthleteActivities.activities.value.isEmpty())
            getActivities()
    }

    private fun getActivities() {
        viewModelScope.launch {
            isLoading.value = true
            if (Oauth2.accessToken == "null") {
                val result = repository.getAccessToken(
                    clientId = 75992,
                    clientSecret = "1393501e79b0abb641468057d790b6df970771f8",
                    code = Oauth2.authorizationCode,
                    grantType = "authorization_code"
                )
                when (result) {
                    is Resource.Success -> {
                        Oauth2.accessToken = result.data.access_token
                    }
                    is Resource.Error -> {
                        loadError.value = result.message
                        isLoading.value = false
                        return@launch
                    }
                }
            }
            when (val result = repository.getActivities()) {
                is Resource.Success -> {
                    AthleteActivities.activities.value = result.data
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError.value = result.message
                    isLoading.value = false
                }
            }
        }
    }
}