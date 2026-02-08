package com.getyourplace.Repository.Interfaces

import com.getyourplace.Models.InterestedProfile

interface IMatchsRepository {
    suspend fun getMatchs(): List<InterestedProfile>
}