package com.getyourplace.Models

enum class RentalStatus(val label: String) {
    COMPLETED("Completed"),
    UPCOMING("Upcoming"),
    CANCELLED("Cancelled");

    // Example helper for Compose UI colors
    fun getColor(): Long {
        return when (this) {
            COMPLETED -> 0xFF4CAF50 // Green
            UPCOMING -> 0xFF2196F3 // Blue
            CANCELLED -> 0xFFF44336 // Red
        }
    }
}