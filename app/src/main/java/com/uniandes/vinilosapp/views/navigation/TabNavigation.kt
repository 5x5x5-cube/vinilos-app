package com.uniandes.vinilosapp.views.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * TabNavigation component that handles the bottom navigation bar for the main app routes: Albums,
 * Artists, and Collectors
 */
@Composable
fun TabNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = Color.Black) {
        NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == "albumes" } == true,
                onClick = {
                    navController.navigate("albumes") {
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
                selected = currentDestination?.hierarchy?.any { it.route == "performers" } == true,
                onClick = {
                    navController.navigate("performers") {
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
                selected = currentDestination?.hierarchy?.any { it.route == "collectors" } == true,
                onClick = {
                    navController.navigate("collectors") {
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
