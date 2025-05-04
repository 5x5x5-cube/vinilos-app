package com.uniandes.vinilosapp.views.collector

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uniandes.vinilosapp.models.Collector
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun CollectorRow(collector: Collector, onVerClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = collector.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Tel: ${collector.telephone}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Email: ${collector.email}", style = MaterialTheme.typography.bodyMedium)
        }
        Button(
            onClick = onVerClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Ver")
        }
    }
}