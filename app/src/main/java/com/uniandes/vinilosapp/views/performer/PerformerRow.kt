package com.uniandes.vinilosapp.views.performer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uniandes.vinilosapp.models.Performer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun PerformerRow(performer: Performer, onVerClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        AsyncImage(
            model = performer.image,
            contentDescription = "Imagen de ${performer.name}",
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = performer.name, style = MaterialTheme.typography.titleMedium)
        }
        Button(
            onClick = onVerClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Ver")
        }
    }
}