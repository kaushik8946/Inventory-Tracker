package com.kaushik.inventorytracker.offline_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OfflineProductDao {
    @Insert
    fun insertProduct(product: OfflineProduct)

    @Query("SELECT * FROM offline_product")
    fun getAllProducts(): List<OfflineProduct>

    @Query("DELETE FROM offline_product")
    fun deleteAllProducts()
}