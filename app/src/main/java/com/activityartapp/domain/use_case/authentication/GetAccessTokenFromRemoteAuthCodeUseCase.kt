package com.activityartapp.domain.use_case.authentication

import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.domain.models.OAuth2
import com.activityartapp.util.Response
import com.activityartapp.util.constants.CLIENT_SECRET
import com.activityartapp.util.constants.TokenConstants.CLIENT_ID
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
    ): Response<OAuth2> {
        return try {
            Response.Success(
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
            Response.Error(exception = e)
        }
    }
}