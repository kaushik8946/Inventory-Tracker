package com.kaushik.inventorytracker.screen.adding

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.kaushik.inventorytracker.R
import com.kaushik.inventorytracker.api.RetrofitInstance
import com.kaushik.inventorytracker.data.ProductResponse
import com.kaushik.inventorytracker.offline_database.AppDatabase
import com.kaushik.inventorytracker.offline_database.OfflineProduct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun ImagePicker(db: AppDatabase) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var selectedImageFile: File? by remember { mutableStateOf(null) }
    var outputText by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launcher.launch("image/*")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Product") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val productName = remember { mutableStateOf("") }
            OutlinedTextField(
                value = productName.value,
                onValueChange = { productName.value = it },
                label = { Text("Product Name") },
            )
            val productType = remember { mutableStateOf("") }
            OutlinedTextField(
                value = productType.value,
                onValueChange = { productType.value = it },
                label = { Text("Product Type") },
            )

            var priceString by remember { mutableStateOf("") }
            val price = remember { mutableFloatStateOf(0.0f) }
            OutlinedTextField(
                value = priceString,
                onValueChange = {
                    priceString = it
                    price.floatValue = it.toFloatOrNull() ?: 0.0f
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text("Price") }
            )

            var taxString by remember { mutableStateOf("") }
            val tax = remember { mutableFloatStateOf(0.0f) }
            OutlinedTextField(
                value = taxString,
                onValueChange = {
                    taxString = it
                    tax.floatValue = it.toFloatOrNull() ?: 0.0f
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text("Tax") }
            )

            if (imageUri != null) {
                // Display the selected image using AsyncImage
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "image picker",
                    modifier = Modifier
                        .size(200.dp)
                        .clickable {
                            // Launch the image picker when the placeholder is clicked
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.READ_MEDIA_IMAGES
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    launcher.launch("image/*")
                                } else {
                                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                }
                            } else {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    launcher.launch("image/*")
                                } else {
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_IMAGES
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }) {
                Text(text = "Select Image")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (productName.value.isBlank() || productType.value.isBlank() || price.floatValue == 0.0f || tax.floatValue == 0.0f) {
                    Toast.makeText(context, "Fields must be non empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val isConnected = isNetworkAvailable(context)
                if (!isConnected) {
                    Toast.makeText(
                        context,
                        "No internet connection, offline mode enabled",
                        Toast.LENGTH_SHORT
                    ).show()
                    GlobalScope.launch {
                        db.offlineProductDao().insertProduct(
                            OfflineProduct(
                                productName = productName.value,
                                productType = productType.value,
                                price = price.floatValue,
                                tax = tax.floatValue,
                                imageUri = imageUri.toString()
                            )
                        )
                    }
                    return@Button
                }

                val productNameBody =
                    productName.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val productTypeBody =
                    productType.value.toRequestBody("text/plain".toMediaTypeOrNull())
                val priceBody =
                    price.floatValue.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val taxBody =
                    tax.floatValue.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                var imagePart: MultipartBody.Part? = null
                selectedImageFile = uriToFile(context, imageUri)
                selectedImageFile?.let { file ->
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    imagePart = MultipartBody.Part.createFormData("files[]", file.name, requestFile)
                }
                val call = RetrofitInstance.apiService.addProduct(
                    productNameBody, productTypeBody, priceBody, taxBody, imagePart
                )
                GlobalScope.launch {
                    val response = postData(call)
                    withContext(Dispatchers.Main) {
                        if (response != null) {
                            Toast.makeText(
                                context,
                                "Product added successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            outputText = response.toString()
                        } else {
                            Toast.makeText(context, "offline mode enabled", Toast.LENGTH_SHORT)
                                .show()
                            db.offlineProductDao().insertProduct(
                                OfflineProduct(
                                    productName = productName.value,
                                    productType = productType.value,
                                    price = price.floatValue,
                                    tax = tax.floatValue,
                                    imageUri = imageUri.toString()
                                )
                            )
                        }
                    }
                }
            }) {
                Text(text = "Submit")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = outputText)
        }
    }
}

fun uriToFile(context: Context, uri: Uri?): File? {
    if (uri == null) {
        return null
    }

    return try {
        // Open an InputStream from the URI
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            // Create a temporary file in the cache directory
            val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

            // Write the InputStream to the File
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
            file
        } ?: run {
            // Return null if InputStream is null (no image found)
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun postData(call: Call<ProductResponse>): ProductResponse? =
    withContext(Dispatchers.IO) {
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}