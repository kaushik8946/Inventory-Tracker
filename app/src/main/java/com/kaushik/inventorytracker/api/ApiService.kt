package com.kaushik.inventorytracker.api

import com.kaushik.inventorytracker.data.Product
import com.kaushik.inventorytracker.data.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("api/public/get")
    fun getData(): Call<List<Product>>

    @Multipart
    @POST("api/public/add")
    fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: MultipartBody.Part? // Optional image
    ): Call<ProductResponse>

}