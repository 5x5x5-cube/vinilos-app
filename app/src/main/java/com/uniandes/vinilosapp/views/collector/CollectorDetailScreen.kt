package com.uniandes.vinilosapp.views.collector

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uniandes.vinilosapp.models.CollectorDetails
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.viewmodels.CollectorDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectorDetailScreen(collectorId: Int, navController: NavController) {
    val context = LocalContext.current.applicationContext as Application
    val viewModel = remember { CollectorDetailsViewModel(context, collectorId) }

    val collector = viewModel.collector.observeAsState().value
    val isNetworkError = viewModel.eventNetworkError.observeAsState(false).value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = collector?.name ?: "Coleccionista", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            collector?.let { CollectorDetailContent(it) }
                ?: if (isNetworkError) {
                    ErrorMessage(onRetry = { viewModel.onNetworkErrorShown() })
                } else {
                    LoadingIndicator()
                }
        }
    }
}

@Composable
fun CollectorDetailContent(collector: CollectorDetails) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Nombre: ${collector.name}", fontSize = 16.sp)
                Text(text = "Teléfono: ${collector.telephone}", fontSize = 16.sp)
                Text(text = "Email: ${collector.email}", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Artistas favoritos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                collector.performers.forEach { performer ->
                    FavoritePerformerItem(performer)
                }
            }
        }
    }
}

@Composable
fun FavoritePerformerItem(performer: Performer) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        AsyncImage(
            model = performer.image,
            contentDescription = "Imagen de ${performer.name}",
            modifier = Modifier
                .size(48.dp)
                .padding(end = 12.dp)
                .then(Modifier),
            contentScale = ContentScale.Crop
        )

        Text(
            text = performer.name,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error al cargar la información del coleccionista",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}
