package com.getyourplace.Models

import android.text.format.DateUtils
import java.util.Date
import java.util.UUID

data class UserReview(
    val id: UUID = UUID.randomUUID(),
    val rating: Double,
    val comment: String,
    val date: Date,
    val reviewerName: String
) {
    /**
     * Converts the date to a relative string (e.g., "2 days ago").
     * Note: This requires Android Context if using DateUtils.getRelativeTimeSpanString,
     * or you can use this simple Kotlin-friendly version:
     */
    val relativeDate: String
        get() {
            val now = System.currentTimeMillis()
            return DateUtils.getRelativeTimeSpanString(
                date.time,
                now,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            ).toString()
        }

    companion object {
        fun mockArray(): List<UserReview> {
            val now = System.currentTimeMillis()
            return listOf(
                UserReview(
                    id = UUID.randomUUID(),
                    rating = 5.0,
                    comment = "Great guest! Highly recommended. Left the place spotless.",
                    date = Date(now - 172800000), // 2 days ago
                    reviewerName = "Sarah"
                ),
                UserReview(
                    id = UUID.randomUUID(),
                    rating = 4.8,
                    comment = "Very communicative and followed all the house rules perfectly.",
                    date = Date(now - 604800000), // 7 days ago
                    reviewerName = "John"
                ),
                UserReview(
                    id = UUID.randomUUID(),
                    rating = 5.0,
                    comment = "Awesome experience, would host again anytime!",
                    date = Date(now - 1209600000), // 14 days ago
                    reviewerName = "Mike"
                )
            )
        }
    }
}