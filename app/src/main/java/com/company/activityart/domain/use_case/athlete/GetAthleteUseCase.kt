package com.company.activityart.domain.use_case.athlete

import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.dataExpired
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.Success
import javax.inject.Inject

class GetAthleteUseCase @Inject constructor(
    private val getAthleteFromLocalUseCase: GetAthleteFromLocalUseCase,
    private val getAthleteFromRemoteUseCase: GetAthleteFromRemoteUseCase,
    private val insertAthleteUseCase: InsertAthleteUseCase,
) {
    suspend operator fun invoke(
        athleteId: Long,
        code: String
    ): Resource<Athlete> {
        getAthleteFromLocalUseCase(athleteId).apply {
            return when {
                this == null || dataExpired -> getAthleteFromRemoteUseCase(code)
                else -> Success(this)
            }.also { if (it is Success) insertAthleteUseCase(it.data) }
        }
    }
}