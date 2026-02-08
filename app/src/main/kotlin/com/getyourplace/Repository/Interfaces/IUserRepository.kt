package com.getyourplace.Repository.Interfaces

import com.getyourplace.Models.RentalHistory
import com.getyourplace.Models.UserProfile
import com.getyourplace.Models.UserReview

interface IUserRepository {
    suspend fun getUserConfiguration(): UserProfile

    suspend fun getUserReviews(): List<UserReview>

    suspend fun getRentalHistory(): List<RentalHistory>
}