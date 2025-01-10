package com.kaushik.inventorytracker

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.kaushik.inventorytracker.api.RetrofitInstance
import com.kaushik.inventorytracker.offline_database.AppDatabase
import com.kaushik.inventorytracker.screen.Navigation
import com.kaushik.inventorytracker.screen.adding.isNetworkAvailable
import com.kaushik.inventorytracker.screen.adding.postData
import com.kaushik.inventorytracker.screen.adding.uriToFile
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = lazy {
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }.value

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) {
                Navigation(db)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            val products = db.offlineProductDao().getAllProducts()
            if (isNetworkAvailable(applicationContext)) {
                products.forEach { product ->
                    val productNameBody =
                        product.productName.toRequestBody("text/plain".toMediaTypeOrNull())
                    val productTypeBody =
                        product.productType.toRequestBody("text/plain".toMediaTypeOrNull())
                    val priceBody =
                        product.price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val taxBody =
                        product.tax.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    var imagePart: MultipartBody.Part? = null
                    val selectedImageFile =
                        uriToFile(applicationContext, Uri.parse(product.imageUri))
                    selectedImageFile?.let { file ->
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        imagePart =
                            MultipartBody.Part.createFormData("files[]", file.name, requestFile)
                    }
                    val call = RetrofitInstance.apiService.addProduct(
                        productNameBody, productTypeBody, priceBody, taxBody, imagePart
                    )
                    val response = postData(call)
                    withContext(Dispatchers.Main) {
                        if (response != null) {
                            Toast.makeText(
                                applicationContext,
                                "Offline Product added successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Failed to add product",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        Log.d("MainActivity", "Response: $response")
                    }
                }
                db.offlineProductDao().deleteAllProducts()
            }
        }
    }
}
