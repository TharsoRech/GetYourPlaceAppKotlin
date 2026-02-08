package com.getyourplace.Models

import java.util.UUID

data class InterestedProfile(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val residenceName: String,
    val imageUrl: String,
    var status: EngagementStatus = EngagementStatus.PENDING
)