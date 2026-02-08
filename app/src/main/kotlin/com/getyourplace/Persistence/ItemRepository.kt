package com.getyourplace.Persistence

class ItemRepository(private val itemDao: ItemDao) {
    // Change 'val' to a 'suspend fun'
    suspend fun getAllItems(): List<Item> {
        return itemDao.getAllItems()
    }
}