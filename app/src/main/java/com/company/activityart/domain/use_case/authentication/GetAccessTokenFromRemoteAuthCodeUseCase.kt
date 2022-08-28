package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.entities.OAuth2Entity
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.OAuth2
import com.company.activityart.util.CLIENT_ID
import com.company.activityart.util.CLIENT_SECRET
import com.company.activityart.util.Resource
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetAccessTokenFromRemoteAuthCodeUseCase @Inject constructor(
    private val api: AthleteApi
) {
    companion object {
        private const val GRANT_TYPE = "authorization_code"
    }

    suspend operator fun invoke(
        authorizationCode: String
    ): Resource<OAuth2> {
        return try {
            Resource.Success(
                api.getAccessToken(
                    clientId = CLIENT_ID,
                    clientSecret = CLIENT_SECRET,
                    code = authorizationCode,
                    grantType = GRANT_TYPE
                )
            )
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Resource.Error(exception = e)
        }
    }
}