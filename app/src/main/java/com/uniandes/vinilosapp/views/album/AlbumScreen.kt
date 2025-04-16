package com.uniandes.vinilosapp.views.album

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uniandes.vinilosapp.models.Album
import androidx.compose.runtime.livedata.observeAsState
import com.uniandes.vinilosapp.viewmodels.AlbumViewModel
import com.uniandes.vinilosapp.views.album.AlbumRow
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(navController: NavController) {

    val application = LocalContext.current.applicationContext as Application

    val albumViewModel: AlbumViewModel = viewModel(factory = AlbumViewModel.Factory(application))

    val albums by albumViewModel.albums.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Álbumes") },
                actions = {
                    IconButton(onClick = {
                        // pendiente la creacion de un album.
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar álbum"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(albums) { album ->
                AlbumRow(album = album, onVerClick = {
                    navController.navigate("albumDetail/${album.albumId}")
                })
                Divider()
            }
        }
    }
}
