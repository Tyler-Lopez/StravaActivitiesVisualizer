package com.activityartapp.domain.use_case.activities

import com.activityartapp.domain.models.Activity
import com.activityartapp.util.Response
import com.activityartapp.util.TimeUtils
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import javax.inject.Inject

class GetActivitiesByYearFromRemoteUseCase @Inject constructor(
    private val getActivitiesInYearByPageFromRemoteUseCase: GetActivitiesByPageFromRemoteUseCase,
    private val timeUtils: TimeUtils
) {
    companion object {
        private const val ACTIVITIES_PER_PAGE = 200
        private const val FIRST_MONTH_OF_YEAR = 0
        private const val LAST_MONTH_OF_YEAR = 11
        private const val FIRST_PAGE = 1
    }

    /**
     * @param startMonth Optional parameter to specify the first 0-indexed month which to read
     * from remote. Activities in months which precede this parameter will be omitted.
     */
    suspend operator fun invoke(
        accessToken: String,
        year: Int,
        startMonth: Int = FIRST_MONTH_OF_YEAR
    ): Response<List<Activity>> {

        var page = FIRST_PAGE
        var activitiesInLastPage = ACTIVITIES_PER_PAGE
        val activities = mutableListOf<Activity>()

        while (activitiesInLastPage >= ACTIVITIES_PER_PAGE) {
            getActivitiesInYearByPageFromRemoteUseCase(
                code = accessToken,
                page = page++,
                activitiesPerPage = ACTIVITIES_PER_PAGE,
                beforeUnixSeconds = timeUtils.firstUnixSecondAfterYearMonth(
                    year, LAST_MONTH_OF_YEAR
                ),
                afterUnixSeconds = timeUtils.lastUnixSecondBeforeYearMonth(
                    year, startMonth
                ),
            )
                .doOnSuccess {
                    activitiesInLastPage = data.size
                    activities.addAll(data)
                }
                .doOnError {
                    /**
                     * Todo, handle error better here
                     * Ex partial return?
                     */
                    return this
                }
        }

        return Response.Success(activities)
    }
}