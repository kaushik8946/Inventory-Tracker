package com.kaushik.inventorytracker.offline_database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OfflineProduct::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun offlineProductDao(): OfflineProductDao
}