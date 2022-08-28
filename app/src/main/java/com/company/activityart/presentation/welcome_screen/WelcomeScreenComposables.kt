package com.company.activityart.presentation.welcome_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.AppVersionNameComposable
import com.company.activityart.presentation.common.ButtonComposable
import com.company.activityart.presentation.ui.theme.Coal
import com.company.activityart.presentation.ui.theme.MaisonNeue
import com.company.activityart.presentation.ui.theme.Silver
import com.company.activityart.presentation.ui.theme.StravaOrange
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent.*

@Composable
fun WelcomeScreenLaunchState(
    athleteId: Long,
    accessToken: String,
    eventReceiver: EventReceiver<WelcomeScreenViewEvent>
) {
    SideEffect { eventReceiver.onEvent(LoadAthlete(athleteId, accessToken)) }
}

@Composable
fun WelcomeScreenLoadError(
    eventReceiver: EventReceiver<WelcomeScreenViewEvent>,
    navController: NavController
) {

}

@Composable
fun WelcomeScreenStandbyState(
    state: Standby,
    eventReceiver: EventReceiver<WelcomeScreenViewEvent>,
    navController: NavController
) {
    Image(
        painter = rememberAsyncImagePainter(state.athleteImageUrl),
        contentDescription = null,
        modifier = Modifier
            .size(156.dp)
            .clip(CircleShape)
            .border(width = 8.dp, color = StravaOrange, shape = CircleShape)
    )
    AppVersionNameComposable()
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Silver,
    ) {
        Text(
            text = state.athleteName,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontFamily = MaisonNeue,
            color = Coal,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

    eventReceiver.apply {
        ButtonComposable(
            text = "Make Art",
            modifier = Modifier.fillMaxWidth()
        ) { onEvent(ClickedMakeArt(navController)) }

        ButtonComposable(
            text = "About",
            modifier = Modifier.fillMaxWidth()
        ) { onEvent(ClickedAbout(navController)) }

        ButtonComposable(
            text = "Logout",
            modifier = Modifier.fillMaxWidth()
        ) { onEvent(ClickedLogout(navController)) }
    }
}
