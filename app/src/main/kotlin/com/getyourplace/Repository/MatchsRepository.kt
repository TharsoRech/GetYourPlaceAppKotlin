package com.getyourplace.Repository

import com.getyourplace.Models.InterestedProfile
import com.getyourplace.Repository.Interfaces.IMatchsRepository
import kotlinx.coroutines.delay

class MatchsRepository : IMatchsRepository {

    override suspend fun getMatchs(): List<InterestedProfile> {
        // Simulating 2-second delay
        // Equivalent to Task.sleep(for: .seconds(2))
        delay(2000)

        return listOf(
            InterestedProfile(name = "Alex Johnson", residenceName = "Sunset Villa", imageUrl = ""),
            InterestedProfile(name = "Sarah Smith", residenceName = "Urban Loft", imageUrl = ""),
            InterestedProfile(name = "Jordan Lee", residenceName = "Sunset Villa", imageUrl = ""),
            InterestedProfile(name = "Taylor Reed", residenceName = "Mountain Cabin", imageUrl = "")
        )
    }
}