package com.getyourplace.Repository

import android.content.Context
import com.getyourplace.Helpers.FilterMatcher
import com.getyourplace.Models.Residence
import com.getyourplace.Models.ResidenceFilter
import com.getyourplace.Repositories.Interfaces.IResidenceRepository
import kotlinx.coroutines.delay

// 1. Add 'context' to the constructor
class ResidenceRepository(private val context: Context) : IResidenceRepository {

    override suspend fun getInterestedResidences(): List<Residence> {
        delay(1000)
        // 2. Pass context to the mock function
        return Residence.mocks(context)
    }

    override suspend fun getFavoritesResidences(): List<Residence> {
        delay(1000)
        return Residence.mocks(context)
    }

    override suspend fun getRecentResidences(): List<Residence> {
        delay(1000)
        return Residence.mocks(context)
    }

    override suspend fun getResidences(nextPage: Int): List<Residence> {
        delay(1000)
        return Residence.mocks(context)
    }

    override suspend fun getPublishResidences(): List<Residence> {
        delay(1000)
        // filter logic stays the same
        return Residence.mocks(context).filter { it.isMine }
    }

    override suspend fun filterResidences(
        residences: List<Residence>,
        filter: ResidenceFilter
    ): List<Residence> {
        return residences.filter { residence ->

            // --- 1. Property-based Filters ---
            if (filter.maxPrice > 0 && residence.price > filter.maxPrice) return@filter false
            if (filter.maxSquareFootage > 0 && residence.squareFootage > filter.maxSquareFootage) return@filter false

            if (filter.citySelected.isNotEmpty() && filter.citySelected != "All") {
                if (!residence.location.contains(filter.citySelected, ignoreCase = true)) {
                    return@filter false
                }
            }

            // --- 2. Selection Dictionary Filters ---
            filter.selections.all { (key, selectedValue) ->
                if (selectedValue == "All" || selectedValue.isEmpty()) return@all true

                when (key) {
                    "Type" -> residence.type == selectedValue
                    "Beds" -> FilterMatcher.check(residence.numberOfBeds, selectedValue)
                    "Rooms" -> FilterMatcher.check(residence.numberOfRooms, selectedValue)
                    "Baths" -> FilterMatcher.check(residence.baths, selectedValue)
                    "Garage" -> FilterMatcher.check(residence.numberOfGarages, selectedValue)
                    else -> true
                }
            }
        }
    }
}