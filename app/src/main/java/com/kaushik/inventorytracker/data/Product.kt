package com.kaushik.inventorytracker.data

import com.google.gson.annotations.SerializedName

data class Product(
    val image: String,
    val price: Float,
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_type") val productType: String,
    val tax: Float
)
