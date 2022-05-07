package com.company.athleteapiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.error_noactivities_screen.ErrorNoActivitiesScreen
import com.company.athleteapiart.presentation.filter_activities_screen.FilterActivitiesScreen
import com.company.athleteapiart.presentation.format_screen.FormatScreenOne
import com.company.athleteapiart.presentation.format_screen.FormatScreenThree
import com.company.athleteapiart.presentation.format_screen.FormatScreenTwo
import com.company.athleteapiart.presentation.format_screen.FormatScreenFour
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.presentation.save_image_screen.SaveImageScreen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreen
import com.company.athleteapiart.presentation.ui.theme.AthleteApiArtTheme
import com.company.athleteapiart.presentation.welcome_screen.WelcomeScreen
import com.company.athleteapiart.util.noAnimComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            AthleteApiArtTheme {

                val navController = rememberAnimatedNavController()

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    AnimatedNavHost(navController, startDestination = Screen.Login.route) {
                        composable(Screen.Login.route) {
                            LoginScreen(
                                uri = intent.data,
                                navController = navController,
                                onLoginIntent = {
                                    // Send us to the screen to connect with Strava and
                                    // prevent going back to this activity after
                                    finish()
                                    startActivity(it)
                                }
                            )
                        }
                        composable(
                            route = Screen.Welcome.route + "/{athleteId}/{accessToken}",
                            arguments = listOf(
                                navArgument("athleteId") {
                                    type = NavType.IntType
                                    nullable = false
                                },
                                navArgument("accessToken") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) { entry ->
                            WelcomeScreen(
                                athleteId = entry.arguments?.getInt("athleteId") ?: -1,
                                accessToken = entry.arguments?.getString("accessToken") ?: "null",
                                navController = navController
                            )
                        }
                        composable(
                            route = Screen.TimeSelect.route + "/{accessToken}",
                            arguments = listOf(
                                navArgument("accessToken") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) {
                            TimeSelectScreen(navController = navController)
                        }
                        composable(Screen.FilterActivities.route) {
                            FilterActivitiesScreen(navController = navController)
                        }
                        composable(Screen.FormatActivitiesOne.route) {
                            FormatScreenOne(navController = navController)
                        }
                        composable(Screen.FormatActivitiesTwo.route) {
                            FormatScreenTwo(navController = navController)
                        }
                        composable(Screen.FormatActivitiesThree.route) {
                            FormatScreenThree(navController = navController)
                        }
                        composable(Screen.FormatActivitiesFour.route) {
                            FormatScreenFour(navController = navController)
                        }
                        composable(Screen.VisualizeActivities.route) {
                            ActivitiesScreen(navController = navController)
                        }
                        composable(Screen.SaveImage.route) {
                            SaveImageScreen(navController = navController)
                        }
                        composable(Screen.ErrorNoActivities.route) {
                            ErrorNoActivitiesScreen(navController = navController)
                        }
                    }
                }

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}