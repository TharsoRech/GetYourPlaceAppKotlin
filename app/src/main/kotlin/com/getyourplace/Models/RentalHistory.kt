package com.getyourplace.Models

import java.util.UUID
import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

data class RentalHistory(
    val id: UUID = UUID.randomUUID(),
    val propertyName: String,
    val location: String,
    val startDate: Date,
    val endDate: Date,
    val status: RentalStatus
) {
    val dateRangeString: String
        get() {
            val startFormatter = SimpleDateFormat("MMM d", Locale.getDefault())
            val endFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            return "${startFormatter.format(startDate)} - ${endFormatter.format(endDate)}"
        }

    companion object {
        private fun createDate(year: Int, month: Int, day: Int): Date {
            return Calendar.getInstance().apply {
                set(year, month - 1, day)
            }.time
        }

        fun mockArray() = listOf(
            RentalHistory(UUID.randomUUID(), "Luxury Villa", "Porto Alegre", createDate(2026, 1, 12), createDate(2026, 1, 20), RentalStatus.COMPLETED),
            RentalHistory(UUID.randomUUID(), "Beachfront Loft", "Florianópolis", createDate(2026, 2, 5), createDate(2026, 2, 12), RentalStatus.UPCOMING),
            RentalHistory(UUID.randomUUID(), "Mountain Cabin", "Gramado", createDate(2025, 12, 20), createDate(2025, 12, 27), RentalStatus.CANCELLED)
        )
    }
}