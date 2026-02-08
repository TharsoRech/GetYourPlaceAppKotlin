package com.getyourplace.Repository

import com.getyourplace.Models.RentalHistory
import com.getyourplace.Models.UserProfile
import com.getyourplace.Models.UserReview
import com.getyourplace.Models.UserRole
import com.getyourplace.Repository.Interfaces.IUserRepository
import kotlinx.coroutines.delay

class UserRepository : IUserRepository {

    override suspend fun getUserReviews(): List<UserReview> {
        // Equivalent to Task.sleep (500ms)
        delay(500)
        return UserReview.mockArray()
    }

    override suspend fun getUserConfiguration(): UserProfile {
        delay(500)
        return UserProfile(
            name = "Melissa Peters",
            email = "melpeters@gmail.com",
            dob = "23/05/1995",
            country = "Nigeria",
            bio = "UI/UX Designer and mobile developer. Passionate about clean code and dark mode interfaces.",
            role = UserRole.OWNER
        )
    }

    override suspend fun getRentalHistory(): List<RentalHistory> {
        // Simulate network delay (1000ms)
        delay(1000)
        return RentalHistory.mockArray()
    }
}