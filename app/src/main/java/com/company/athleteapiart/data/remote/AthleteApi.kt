package com.company.athleteapiart.data.remote

import com.company.athleteapiart.data.remote.responses.Activities
import com.company.athleteapiart.data.remote.responses.ActivityDetailed
import com.company.athleteapiart.data.remote.responses.Bearer
import retrofit2.http.*

interface AthleteApi {
    @POST("oauth/token?")
    suspend fun getAccessToken(
        @Query("client_id") clientId: Int,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Query("grant_type") grantType: String
    ): Bearer

    @POST("oauth/token?")
    suspend fun getAccessTokenFromRefresh(
        @Query("client_id") clientId: Int,
        @Query("client_secret") clientSecret: String,
        @Query("refresh_token") refreshToken: String,
        @Query("grant_type") grantType: String = "refresh_token"
    ): Bearer

    // Get activities from access token
    @GET("api/v3/athlete/activities?")
    suspend fun getActivities(
        @Header("Authorization") authHeader: String,
        @Query("before") before: Int,
        @Query("after") after: Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Activities

    @GET("api/v3/activities/{id}?")
    suspend fun getActivityDetailed(
        @Path("id") id: Long,
        @Header("Authorization") authHeader: String,
    ) : ActivityDetailed
}