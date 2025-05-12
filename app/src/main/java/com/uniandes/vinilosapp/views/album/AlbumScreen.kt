package com.uniandes.vinilosapp.views.album

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.uniandes.vinilosapp.viewmodels.AlbumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(navController: NavController) {

    val application = LocalContext.current.applicationContext as Application

    val albumViewModel: AlbumViewModel = viewModel(factory = AlbumViewModel.Factory(application))

    val albums by albumViewModel.albums.observeAsState(initial = emptyList())

    // Check if we need to refresh albums when navigating back from CreateAlbumScreen
    LaunchedEffect(Unit) {
        // Get the refresh flag from the SavedStateHandle
        val needsRefresh =
                navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>(
                        "refresh_albums"
                )
                        ?: false

        // If the flag is set, refresh albums and reset the flag
        if (needsRefresh) {
            albumViewModel.refreshAlbums()
            navController.currentBackStackEntry?.savedStateHandle?.set("refresh_albums", false)
        }
    }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Álbumes", fontWeight = FontWeight.Bold) },
                        actions = {
                            IconButton(onClick = { navController.navigate("albumes/create") }) {
                                Icon(
                                        imageVector = Icons.Rounded.AddCircle,
                                        contentDescription = "Agregar álbum",
                                        modifier = Modifier.size(size = 36.dp),
                                        tint = Color.Black
                                )
                            }
                        }
                )
            }
    ) { innerPadding ->
        LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)
        ) {
            items(albums) { album ->
                AlbumRow(
                        album = album,
                        onVerClick = { navController.navigate("albumes/${album.albumId}") }
                )
                Divider()
            }
        }
    }
}
