package com.getyourplace.Repository.Interfaces

import com.getyourplace.Models.ResidenceFilter

interface IFilterRepository {
    suspend fun getDefaultFilters(): List<String>

    suspend fun getCustomFilters(): ResidenceFilter
}