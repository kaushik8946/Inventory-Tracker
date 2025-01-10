package com.kaushik.inventorytracker.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.kaushik.inventorytracker.data.Product
import com.kaushik.inventorytracker.screen.listing.productList

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val filteredProducts = remember(searchQuery, productList.value) {
        productList.value.filter { product ->
            product.productName.contains(searchQuery, ignoreCase = true) ||
                    product.productType.contains(searchQuery, ignoreCase = true)
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        // Search Input Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by Name or Type of Products") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display Filtered Products
        LazyColumn {
            items(filteredProducts) { product ->
                ProductItem(product)
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    val defaultUrl =
        "https://vx-erp-product-images.s3.ap-south-1.amazonaws.com/9_1735912543_0_image.jpg"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Image Placeholder
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
        ){
            val imageUrl = product.image.ifEmpty { defaultUrl }
            val painter = rememberAsyncImagePainter(imageUrl)
            val painterState = painter.state
            Image(
                painter = painter,
                contentDescription = product.productName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Show a spinner if loading
            if (painterState is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Show error message if image fails to load
            if (painterState is AsyncImagePainter.State.Error) {
                Text(
                    text = "Failed to load image",
                    color = Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = product.productName, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Price: ${product.price}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Type: ${product.productType}", style = MaterialTheme.typography.bodySmall)
        }
    }
}