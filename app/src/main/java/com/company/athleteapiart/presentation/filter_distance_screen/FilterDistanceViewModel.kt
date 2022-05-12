package com.company.athleteapiart.presentation.filter_distance_screen

import androidx.lifecycle.ViewModel
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterDistanceViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

}