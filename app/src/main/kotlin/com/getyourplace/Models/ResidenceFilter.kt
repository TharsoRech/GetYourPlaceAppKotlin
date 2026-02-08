package com.getyourplace.Models

data class ResidenceFilter(
    var maxPrice: Double = 0.0,
    var maxSquareFootage: Double = 0.0,
    // CHANGE: Use MutableMap instead of Map
    var selections: MutableMap<String, String> = mutableMapOf(),
    var pickerOptions: Map<String, List<String>> = emptyMap(),
    var cities: List<String> = emptyList(),
    var citySelected: String = ""
) {
    companion object {
        fun mock(): ResidenceFilter {
            return ResidenceFilter(
                maxPrice = 5000.0,
                maxSquareFootage = 2000.0,
                // Ensure this starts as a mutable map
                selections = mutableMapOf(),
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
}