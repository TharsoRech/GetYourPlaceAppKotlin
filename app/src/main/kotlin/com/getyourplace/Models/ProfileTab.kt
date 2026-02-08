package com.getyourplace.Models

enum class ProfileTab(val label: String, val icon: String) {
    ACCOUNT("Account", "ic_person"),
    PERSONAL("Personal", "ic_info"),
    RENTALS("Rentals", "ic_house"),
    REVIEWS("Reviews", "ic_star");

    companion object {
        val entriesList = entries // Equivalent to CaseIterable
    }
}