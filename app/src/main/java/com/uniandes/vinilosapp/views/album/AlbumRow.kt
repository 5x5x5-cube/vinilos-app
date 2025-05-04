package com.uniandes.vinilosapp.views.album

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uniandes.vinilosapp.models.Album
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun AlbumRow(album: Album, onVerClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        AsyncImage(
            model = album.cover,
            contentDescription = "Portada de ${album.name}",
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = album.name, style = MaterialTheme.typography.titleMedium)
            Text(text = album.recordLabel, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
        }
        Button(onClick = onVerClick, colors=ButtonDefaults.buttonColors(
            containerColor = Color.Black)) {
            Text("Ver")
        }
    }
}