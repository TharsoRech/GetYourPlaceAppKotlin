package com.getyourplace.Repository


import com.getyourplace.Repository.Interfaces.INotificationRepository
import kotlinx.coroutines.delay

class NotificationRepository : INotificationRepository {

    override suspend fun getNotifications(): List<String> {
        delay(1000)

        return listOf(
            "James showed interest in your residence",
            "New login from Chrome",
            "Your subscription was renewed"
        )
    }
}