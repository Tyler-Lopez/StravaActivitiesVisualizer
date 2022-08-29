package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.domain.models.OAuth2
import com.company.activityart.domain.models.requiresRefresh
import javax.inject.Inject

class GetAccessTokenFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(): OAuth2? {
        return athleteDatabase
            .oAuth2Dao
            .getCurrAuth()
    }
}