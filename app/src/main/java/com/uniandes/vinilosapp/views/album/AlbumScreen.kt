package com.uniandes.vinilosapp.views.album

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.views.album.AlbumRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen() {
    // Lista de álbumes de ejemplo. En la práctica, esta data provendría de tu ViewModel.
    val albums = remember { sampleAlbums() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Álbumes") },
                actions = {
                    IconButton(onClick = {
                        // Lógica para agregar un nuevo álbum
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
                    // Aquí manejas la acción "Ver", por ejemplo, navegando a detalles del álbum.
                })
                Divider()
            }
        }
    }
}

// Función de ejemplo para poblar la lista de álbumes
fun sampleAlbums(): List<Album> = listOf(
    Album(1, "Lemonade", "https://i.imgur.com/Example1.jpg", "2016-04-23", "Descripción 1", "Pop", "Parkwood Entertainment"),
    Album(2, "Bachata Rosa", "https://i.imgur.com/Example2.jpg", "1990-10-01", "Descripción 2", "Bachata", "RCA"),
    Album(3, "Like a Prayer", "https://i.imgur.com/Example3.jpg", "1989-03-21", "Descripción 3", "Pop Rock", "Sire"),
    Album(4, "Thriller", "https://i.imgur.com/Example4.jpg", "1982-11-30", "Descripción 4", "Pop", "Epic"),
    Album(5, "The Dark Side of the Moon", "https://i.imgur.com/Example5.jpg", "1973-03-01", "Descripción 5", "Rock", "Harvest")
)
