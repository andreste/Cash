package com.example.cashapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

enum class Screen {
    Home
}

fun NavGraphBuilder.composable(
    screen: Screen,
    content: @Composable (NavBackStackEntry) -> Unit,
) = composable(screen.name, content = content)

fun NavController.navigate(screen: Screen) = navigate(screen.name)