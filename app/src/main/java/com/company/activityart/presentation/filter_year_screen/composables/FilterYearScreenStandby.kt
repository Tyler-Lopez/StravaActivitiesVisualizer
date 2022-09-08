package com.company.activityart.presentation.filter_year_screen.composables

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.domain.models.Activity
import com.company.activityart.presentation.common.button.ButtonSize.*
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent

@Composable
fun FilterYearScreenStandby(
    activitiesByYear: List<Pair<Int, List<Activity>>>,
    eventReceiver: ViewEventListener<FilterYearViewEvent>,
    isLoadingActivities: Boolean,
) {
    LazyColumn {
        items(activitiesByYear.size) {
            Text(text = activitiesByYear[it].second.size.toString())
        }
        item {
            if (isLoadingActivities) {
                CircularProgressIndicator()
            }
        }
    }
    HighEmphasisButton(
        text = stringResource(R.string.button_continue),
        modifier = Modifier.defaultMinSize(
            minWidth = dimensionResource(id = R.dimen.button_min_width)
        ),
        size = LARGE
    ) {
        eventReceiver.onEventDebounced(ContinueClicked)
    }
}