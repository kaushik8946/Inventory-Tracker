package com.kaushik.inventorytracker.data

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val message: String,
    @SerializedName("product_details") val productDetails: Product?,
    @SerializedName("product_id") val productId: Int,
    val success: Boolean
)