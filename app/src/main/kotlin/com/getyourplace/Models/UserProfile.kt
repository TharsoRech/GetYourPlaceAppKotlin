package com.getyourplace.Models

import java.util.UUID

data class UserProfile(
    var id: UUID = UUID.randomUUID(),
    var name: String? = null,
    var email: String? = null,
    var password: String? = null, // Added password field
    var dob: String? = null,
    var country: String? = null,
    var bio: String? = null,
    var role: UserRole? = null,
    var base64Image: String? = null,
    var profession: String? = null
) {
    companion object {
        fun mock() = UserProfile(
            name = "Alex Sterling",
            email = "alex.sterling@example.com",
            password = "password123", // Added to mock
            role = UserRole.OWNER,
            profession = "Architect"
        )
    }
}