package com.kaushik.inventorytracker.data

sealed class ProductListState {
    data object Loading : ProductListState()
    data class Success(val products: List<Product>) : ProductListState()
    data class Error(val message: String) : ProductListState()
}