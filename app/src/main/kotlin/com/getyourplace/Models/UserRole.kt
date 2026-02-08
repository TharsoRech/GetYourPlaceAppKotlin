package com.getyourplace.Models

enum class UserRole(
    val title: String,
    val description: String,
    val iconName: String
) {
    OWNER(
        title = "Owner",
        description = "I want to list my property",
        iconName = "ic_house_fill" // In Android, use snake_case for drawable resources
    ),
    RENTER(
        title = "Renter",
        description = "I am looking for a place",
        iconName = "ic_person_fill"
    );

    companion object {
        // Equivalent to CaseIterable: UserRole.entries
        // Equivalent to Codable: Use @Serializable if using Kotlin Serialization

        fun fromTitle(title: String): UserRole? {
            return entries.find { it.title == title }
        }
    }
}