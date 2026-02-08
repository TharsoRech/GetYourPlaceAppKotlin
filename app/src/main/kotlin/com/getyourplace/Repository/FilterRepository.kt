package com.getyourplace.Repository

import com.getyourplace.Models.ResidenceFilter
import com.getyourplace.Repository.Interfaces.IFilterRepository
import kotlinx.coroutines.delay

class FilterRepository : IFilterRepository {

    override suspend fun getDefaultFilters(): List<String> {
        // Simulating 1-second delay
        delay(1000)

        return listOf("Newest", "Price", "Rating", "Category")
    }

    override suspend fun getCustomFilters(): ResidenceFilter {
        // Simulating 1-second delay
        delay(1000)

        return ResidenceFilter(
            maxPrice = 10000000.0,
            maxSquareFootage = 2000.0,
            selections = emptyMap(), // Equivalent to Swift's [:]
            pickerOptions = mapOf(
                "Type" to listOf("All", "Apartment", "House", "Villa", "Studio"),
                "Beds" to listOf("None", "1", "2", "3", "4+"),
                "Rooms" to listOf("None", "1", "2", "3", "4+"),
                "Garage" to listOf("None", "1", "2", "3", "4+"),
                "Baths" to listOf("None", "1", "2", "3", "4+")
            ),
            cities = listOf("New York", "Rio de Janeiro", "Budapest", "Atlanta", "London")
        )
    }
}