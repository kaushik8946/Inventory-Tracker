package com.kaushik.inventorytracker.offline_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_product")
data class OfflineProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val productName: String,
    @ColumnInfo val productType: String,
    @ColumnInfo val price: Float,
    @ColumnInfo val tax: Float,
    @ColumnInfo val imageUri: String
)