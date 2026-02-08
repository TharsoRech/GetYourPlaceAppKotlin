package com.getyourplace.Repository.Interfaces

interface INotificationRepository {
    suspend fun getNotifications(): List<String>
}