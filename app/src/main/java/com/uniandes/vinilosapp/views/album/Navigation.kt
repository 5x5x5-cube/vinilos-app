package com.uniandes.vinilosapp.views.album

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uniandes.vinilosapp.views.collector.CollectorScreen
import com.uniandes.vinilosapp.views.performer.PerformerScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show tabs only on main screens, not on detail screens
    val shouldShowTabs = currentRoute != null && !currentRoute.startsWith("albumDetail/")

    Scaffold(
            bottomBar = {
                if (shouldShowTabs) {
                    BottomTabNavigation(navController)
                }
            }
    ) { innerPadding ->
        NavHost(
                navController = navController,
                startDestination = "album",
                modifier = Modifier.padding(innerPadding)
        ) {
            // Album routes
            composable("album") { AlbumsScreen(navController = navController) }
            composable(
                    route = "albumDetail/{albumId}",
                    arguments = listOf(navArgument("albumId") { type = NavType.IntType })
            ) { backStackEntry ->
                val albumId = backStackEntry.arguments?.getInt("albumId") ?: 0
                AlbumDetailScreen(albumId = albumId, navController = navController)
            }

            // Performer route (main screen only)
            composable("performer") { PerformerScreen(navController = navController) }

            // Collector route (main screen only)
            composable("collector") { CollectorScreen(navController = navController) }
        }
    }
}

@Composable
fun BottomTabNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = Color.Black) {
        NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == "album" } == true,
                onClick = {
                    navController.navigate("album") {
                        // Pop up to the start destination of the graph to avoid building up a large
                        // stack
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        // Avoid multiple copies of the same destination when reselecting the same
                        // item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                label = { Text("Álbumes", color = Color.White) },
                colors =
                        NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color.DarkGray
                        ),
                icon = { Icon(Icons.Filled.Album, contentDescription = "Albums") },
                alwaysShowLabel = true
        )

        NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == "performer" } == true,
                onClick = {
                    navController.navigate("performer") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text("Artistas", color = Color.White) },
                colors =
                        NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color.DarkGray
                        ),
                icon = { Icon(Icons.Filled.Person, contentDescription = "Artists") },
                alwaysShowLabel = true
        )

        NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == "collector" } == true,
                onClick = {
                    navController.navigate("collector") {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text("Coleccionistas", color = Color.White) },
                colors =
                        NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color.DarkGray
                        ),
                icon = { Icon(Icons.Filled.Group, contentDescription = "Collectors") },
                alwaysShowLabel = true
        )
    }
}
