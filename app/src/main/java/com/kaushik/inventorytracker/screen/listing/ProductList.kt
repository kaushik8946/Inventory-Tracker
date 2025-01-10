package com.kaushik.inventorytracker.screen.listing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.kaushik.inventorytracker.data.Product

@Composable
fun ProductList(products: List<Product>, paddingValues: PaddingValues) {
    val defaultUrl =
        "https://vx-erp-product-images.s3.ap-south-1.amazonaws.com/9_1735912543_0_image.jpg"
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        items(products.size) { index ->
            val fraction = if (index % 2 == 0) 0.5f else 1f
            val product = products[index]
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    // Image Box with Loading State
                    val imageUrl = product.image.ifEmpty { defaultUrl }
                    val painter = rememberAsyncImagePainter(imageUrl)
                    val painterState = painter.state

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        // Display the image
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // Product Details
                    Text(
                        text = product.productName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = product.productType,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product.price.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Green
                        )
                        Text(
                            text = "Tax: ${product.tax}",
                            fontSize = 14.sp,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}