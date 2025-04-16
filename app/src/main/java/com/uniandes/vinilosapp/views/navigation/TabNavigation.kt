package com.uniandes.vinilosapp.views.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

    Column {
        Divider(color = Color.LightGray, thickness = 1.dp)
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "albumes" } == true,
                    onClick = {
                        navController.navigate("albumes") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Álbumes", color = Color.Black) },
                    colors =
                            NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.Black,
                                    selectedTextColor = Color.Black,
                                    indicatorColor = Color.LightGray,
                                    unselectedIconColor = Color.DarkGray,
                                    unselectedTextColor = Color.DarkGray
                            ),
                    icon = {
                        Icon(
                                Icons.Filled.MusicNote,
                                contentDescription = "Albums",
                                tint = Color.Black
                        )
                    },
                    alwaysShowLabel = true
            )

            NavigationBarItem(
                    selected =
                            currentDestination?.hierarchy?.any { it.route == "performers" } == true,
                    onClick = {
                        navController.navigate("performers") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Artistas", color = Color.Black) },
                    colors =
                            NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.Black,
                                    selectedTextColor = Color.Black,
                                    indicatorColor = Color.LightGray,
                                    unselectedIconColor = Color.DarkGray,
                                    unselectedTextColor = Color.DarkGray
                            ),
                    icon = {
                        Icon(Icons.Filled.Star, contentDescription = "Artists", tint = Color.Black)
                    },
                    alwaysShowLabel = true
            )

            NavigationBarItem(
                    selected =
                            currentDestination?.hierarchy?.any { it.route == "collectors" } == true,
                    onClick = {
                        navController.navigate("collectors") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text("Coleccionistas", color = Color.Black) },
                    colors =
                            NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.Black,
                                    selectedTextColor = Color.Black,
                                    indicatorColor = Color.LightGray,
                                    unselectedIconColor = Color.DarkGray,
                                    unselectedTextColor = Color.DarkGray
                            ),
                    icon = {
                        Icon(
                                Icons.Filled.Group,
                                contentDescription = "Collectors",
                                tint = Color.Black
                        )
                    },
                    alwaysShowLabel = true
            )
        }
    }
}
