package com.company.athleteapiart.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.company.athleteapiart.data.entities.OAuth2Entity

@Dao
interface OAuth2Dao {

    @Insert
    suspend fun insertOauth2(oAuth2Entity: OAuth2Entity)

    // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all
    @Query("DELETE FROM oauth2entity")
    suspend fun clearOauth2()

    @Query("SELECT * FROM oauth2entity")
    suspend fun getOauth2(): OAuth2Entity?

}