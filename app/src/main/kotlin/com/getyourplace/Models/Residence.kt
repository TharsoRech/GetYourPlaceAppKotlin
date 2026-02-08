package com.getyourplace.Models

import android.content.Context
import com.getyourplace.Helpers.asAssetBase64
import java.util.UUID
import java.util.Date
import java.text.NumberFormat
import java.util.Locale

data class Residence(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var description: String,
    var address: String,
    var location: String,
    var type: String,
    var price: Double,
    var numberOfRooms: Int,
    var numberOfBeds: Int,
    var baths: Int,
    var squareFootage: Double,
    var hasGarage: Boolean,
    var numberOfGarages: Int,
    var acceptPets: Boolean,
    var rating: Double = 0.0,
    var createdAt: Date = Date(),
    var isPublished: Boolean = false,
    var mainImageBase64: String,
    var galleryImagesBase64: List<String> = emptyList(),
    var favorite: Boolean = false,
    var isMine: Boolean = false
) {
    // --- Formatters (Equivalent to Swift Computed Properties) ---

    val formattedPrice: String
        get() = NumberFormat.getCurrencyInstance(Locale.US).apply {
            maximumFractionDigits = 0
        }.format(price)

    val formattedLocation: String get() = "$location, $address"
    val formattedNumberOfBeds: String get() = "$numberOfBeds Beds"
    val formattedNumberOfRooms: String get() = "$numberOfRooms Rooms"
    val formattedNumberOfGarages: String get() = "$numberOfGarages Garage"
    val formattedNumberOfBaths: String get() = "$baths Baths"
    val petsStatus: String get() = if (acceptPets) "Pets Allowed" else "No Pets"

    companion object {
        fun mock(context: Context) = Residence(
            name = "Modern Villa",
            description = "A beautiful modern villa with sea views and a private pool.",
            address = "123 luxury Way",
            location = "Los Angeles, CA",
            type = "House",
            price = 2500000.0,
            numberOfRooms = 8,
            numberOfBeds = 4,
            baths = 3,
            squareFootage = 3500.0,
            hasGarage = true,
            numberOfGarages = 1,
            acceptPets = true,
            rating = 5.0,
            mainImageBase64 = "house1".asAssetBase64(context),
            isPublished = true
        )

        fun mocks(context: Context) = listOf(
            Residence(
                name = "Modern Villa",
                description = "A beautiful modern villa with sea views and a private pool.",
                address = "123 luxury Way",
                location = "Los Angeles, CA",
                type = "House",
                price = 2500000.0,
                numberOfRooms = 8,
                numberOfBeds = 4,
                baths = 3,
                squareFootage = 3500.0,
                hasGarage = true,
                numberOfGarages = 1,
                acceptPets = true,
                rating = 5.0,
                mainImageBase64 = "house2".asAssetBase64(context)
            ),
            Residence(
                name = "Skyline Apartment",
                description = "Luxury high-rise living with floor-to-ceiling windows.",
                address = "888 Central Ave",
                location = "New York, NY",
                type = "Apartment",
                price = 950000.0,
                numberOfRooms = 3,
                numberOfBeds = 1,
                baths = 1,
                squareFootage = 850.0,
                hasGarage = false,
                numberOfGarages = 0,
                acceptPets = false,
                rating = 4.5,
                mainImageBase64 = "house3".asAssetBase64(context),
                favorite = true
            ),
            Residence(
                name = "Cozy Cottage",
                description = "A rustic getaway tucked in the mountains.",
                address = "42 Forest Road",
                location = "Aspen, CO",
                type = "House",
                price = 1200000.0,
                numberOfRooms = 5,
                numberOfBeds = 3,
                baths = 2,
                squareFootage = 1800.0,
                hasGarage = true,
                numberOfGarages = 1,
                acceptPets = true,
                rating = 4.8,
                mainImageBase64 = "house3".asAssetBase64(context),
                isMine = true
            )
        )
    }
}