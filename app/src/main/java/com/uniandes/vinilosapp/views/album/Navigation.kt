package com.uniandes.vinilosapp.views.album

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uniandes.vinilosapp.views.album.AlbumRow
import com.uniandes.vinilosapp.views.album.AlbumsScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "albums") {
        composable("albums") {
            AlbumsScreen(navController = navController)
        }
        composable(
            route = "albumDetail/{albumId}",
            arguments = listOf(
                navArgument("albumId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getInt("albumId") ?: 0
            AlbumDetailScreen(albumId = albumId, navController = navController )
        }
    }
}
