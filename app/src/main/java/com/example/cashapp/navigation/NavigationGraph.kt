package com.example.cashapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.cashapp.screens.HomeScreen
import com.example.cashapp.viewmodel.StocksViewModel

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val viewModel: StocksViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.Home.name) {
        composable(Screen.Home) {
            HomeScreen(navController, viewModel)
        }
    }
}