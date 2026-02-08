package com.getyourplace.Persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Item::class], version = 1)
@TypeConverters(DateConverter::class) // Room needs help with Date objects
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Equivalent to PersistenceController.shared
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "get_your_place_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        // Equivalent to PersistenceController.preview
        fun getPreviewDatabase(context: Context): AppDatabase {
            val db = Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                AppDatabase::class.java
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Pre-populate with 10 items for the preview
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = getPreviewDatabase(context).itemDao()
                        repeat(10) {
                            dao.insert(Item())
                        }
                    }
                }
            }).build()
            return db
        }
    }
}