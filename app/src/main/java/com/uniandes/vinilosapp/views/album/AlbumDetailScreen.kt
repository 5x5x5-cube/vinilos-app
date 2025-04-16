package com.uniandes.vinilosapp.views.album

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uniandes.vinilosapp.models.AlbumDetails
import com.uniandes.vinilosapp.models.Track
import com.uniandes.vinilosapp.viewmodels.AlbumDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(albumId: Int, navController: NavController) {
    val context = LocalContext.current.applicationContext as Application
    val viewModel = remember { AlbumDetailViewModel(context, albumId) }

    val album = viewModel.album.observeAsState().value
    val isNetworkError = viewModel.eventNetworkError.observeAsState(false).value

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text(text = album?.name ?: "Album Details") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Go back"
                                )
                            }
                        }
                )
            }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            album?.let { albumDetails -> AlbumDetailContent(albumDetails) }
                    ?: run {
                        if (isNetworkError) {
                            ErrorMessage(onRetry = { viewModel.onNetworkErrorShown() })
                        } else {
                            LoadingIndicator()
                        }
                    }
        }
    }
}

@Composable
fun AlbumDetailContent(album: AlbumDetails) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            AsyncImage(
                    model = album.cover,
                    contentDescription = "Portada de ${album.name}",
                    modifier =
                            Modifier.fillMaxWidth()
                                    .height(280.dp)
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                        text = "Fecha de lanzamiento: ${album.releaseDate}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                        text = "Género: ${album.genre}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                        text = "Descripción",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )

                Text(
                        text = album.description,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                        text = "Sello discográfico: ${album.recordLabel}",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                )

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                Text(
                        text = "Tracks",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        items(album.tracks) { track -> TrackItem(track) }
        items(album.tracks) { track -> TrackItem(track) }
        items(album.tracks) { track -> TrackItem(track) }
        items(album.tracks) { track -> TrackItem(track) }
        items(album.tracks) { track -> TrackItem(track) }
        items(album.tracks) { track -> TrackItem(track) }
    }
}

@Composable
fun TrackItem(track: Track) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = track.name, fontSize = 16.sp)
        Text(text = track.duration, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(onRetry: () -> Unit) {
    Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(
                text = "Error al cargar la información del álbum",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}
