package com.kaushik.inventorytracker.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kaushik.inventorytracker.offline_database.AppDatabase
import com.kaushik.inventorytracker.screen.adding.ImagePicker
import com.kaushik.inventorytracker.screen.listing.ListingScreen
import com.kaushik.inventorytracker.screen.search.SearchScreen

@Composable
fun Navigation(db: AppDatabase) {
    val navController = rememberNavController()
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = "list") {
                composable("list") {
                    ListingScreen(navController)
                }
                composable("search") {
                    SearchScreen()
                }
                composable("add") {
                    ImagePicker(db)
                }
            }
        }
    }
}