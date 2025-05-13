package com.uniandes.vinilosapp.views.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uniandes.vinilosapp.models.PerformerType
import com.uniandes.vinilosapp.views.album.AddTrackScreen
import com.uniandes.vinilosapp.views.album.AlbumDetailScreen
import com.uniandes.vinilosapp.views.album.AlbumsScreen
import com.uniandes.vinilosapp.views.album.CreateAlbumScreen
import com.uniandes.vinilosapp.views.collector.CollectorDetailScreen
import com.uniandes.vinilosapp.views.collector.CollectorScreen
import com.uniandes.vinilosapp.views.performer.PerformerDetailScreen
import com.uniandes.vinilosapp.views.performer.PerformerScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show tabs only on main screens, not on detail screens
    val shouldShowTabs =
            currentRoute != null &&
                    !currentRoute.startsWith("albumes/") &&
                    !currentRoute.equals("collectors/") &&
                    !currentRoute.startsWith("performers/")

    Scaffold(
            bottomBar = {
                if (shouldShowTabs) {
                    TabNavigation(navController)
                }
            }
    ) { innerPadding ->
        NavHost(
                navController = navController,
                startDestination = "albumes",
                modifier = Modifier.padding(innerPadding)
        ) {
            // Album routes
            composable("albumes") { AlbumsScreen(navController = navController) }
            composable("albumes/create") { CreateAlbumScreen(navController = navController) }
            composable(
                    route = "albumes/{albumId}",
                    arguments = listOf(navArgument("albumId") { type = NavType.IntType })
            ) { backStackEntry ->
                val albumId = backStackEntry.arguments?.getInt("albumId") ?: 0
                AlbumDetailScreen(albumId = albumId, navController = navController)
            }

            // Add Track route
            composable(
                    route = "albumes/{albumId}/add-track?albumName={albumName}",
                    arguments =
                            listOf(
                                    navArgument("albumId") { type = NavType.IntType },
                                    navArgument("albumName") {
                                        type = NavType.StringType
                                        nullable = true
                                    }
                            )
            ) { backStackEntry ->
                val albumId = backStackEntry.arguments?.getInt("albumId") ?: 0
                val albumName = backStackEntry.arguments?.getString("albumName") ?: ""
                AddTrackScreen(
                        albumId = albumId,
                        albumName = albumName,
                        navController = navController
                )
            }

            // Performer routes
            composable("performers") { PerformerScreen(navController = navController) }

            // Unified performer detail route
            composable(
                    route = "performers/{performerId}?type={performerType}",
                    arguments =
                            listOf(
                                    navArgument("performerId") { type = NavType.IntType },
                                    navArgument("performerType") {
                                        type = NavType.StringType
                                        nullable = true
                                    }
                            )
            ) { backStackEntry ->
                val performerId = backStackEntry.arguments?.getInt("performerId") ?: 0
                val performerTypeStr = backStackEntry.arguments?.getString("performerType")
                val performerType =
                        performerTypeStr?.let {
                            try {
                                PerformerType.valueOf(it)
                            } catch (e: IllegalArgumentException) {
                                null
                            }
                        }

                PerformerDetailScreen(
                        performerId = performerId,
                        performerType = performerType,
                        navController = navController
                )
            }

            // Collector route (main screen only)
            composable("collectors") { CollectorScreen(navController = navController) }
            composable(
                route = "collectors/{collectorId}",
                arguments = listOf(navArgument("collectorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val collectorId = backStackEntry.arguments?.getInt("collectorId") ?: 0
                CollectorDetailScreen(collectorId = collectorId, navController = navController)
            }
        }
    }
}
