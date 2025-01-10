package com.kaushik.inventorytracker.screen.listing

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kaushik.inventorytracker.api.RetrofitInstance
import com.kaushik.inventorytracker.data.Product
import com.kaushik.inventorytracker.data.ProductListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListingScreen(navController: NavHostController) {
    var productListState: ProductListState by remember { mutableStateOf(ProductListState.Loading) }
    LaunchedEffect(Unit) {
        productListState = try {
            val products = fetchData()
            if (products != null) {
                productList.value = products
                ProductListState.Success(products)
            } else {
                ProductListState.Error("Failed to fetch products")
            }
        } catch (e: Exception) {
            ProductListState.Error("Network error: ${e.message}")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products") },
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        modifier = Modifier.padding(5.dp)
    ) { paddingValues ->
        when (productListState) {
            is ProductListState.Loading -> LoadingSpinner()
            is ProductListState.Error -> ErrorScreen((productListState as ProductListState.Error).message)
            is ProductListState.Success -> ProductList(
                (productListState as ProductListState.Success).products,
                paddingValues
            )
        }
    }
}

@Composable
fun LoadingSpinner() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
        )
        Text(text = "Loading...")
    }
}

suspend fun fetchData(): List<Product>? = withContext(Dispatchers.IO) {
    try {
        val response = RetrofitInstance.apiService.getData().execute()
        if (response.isSuccessful) {
            response.body()
        } else {
            null // Return null on failure
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
        null // Return null on exceptions
    }
}

@Composable
fun ErrorScreen(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Error: $message")
    }
}

val productList = mutableStateOf(listOf<Product>())