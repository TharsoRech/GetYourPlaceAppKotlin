package com.getyourplace.Repositories.Interfaces

import com.getyourplace.Models.Residence
import com.getyourplace.Models.ResidenceFilter

interface IResidenceRepository {
    suspend fun getRecentResidences(): List<Residence>

    suspend fun getResidences(nextPage: Int): List<Residence>

    suspend fun filterResidences(residences: List<Residence>, filter: ResidenceFilter): List<Residence>

    suspend fun getPublishResidences(): List<Residence>

    suspend fun getFavoritesResidences(): List<Residence>

    suspend fun getInterestedResidences(): List<Residence>
}