package com.uniandes.vinilosapp.views.performer

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
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.models.PerformerType
import com.uniandes.vinilosapp.viewmodels.PerformerDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformerDetailScreen(
        performerId: Int,
        performerType: PerformerType? = null,
        navController: NavController
) {
    val context = LocalContext.current.applicationContext as Application
    val viewModel = remember { PerformerDetailViewModel(context, performerId, performerType) }

    val performer = viewModel.performer.observeAsState().value
    val isNetworkError = viewModel.eventNetworkError.observeAsState(false).value

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(text = performer?.name ?: "Artista", fontWeight = FontWeight.Bold)
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                        contentDescription = "Go back"
                                )
                            }
                        }
                )
            },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            performer?.let { performerDetails: Performer ->
                PerformerDetailContent(performerDetails)
            }
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
fun PerformerDetailContent(performer: Performer) {
    LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                    model = performer.image,
                    contentDescription = "Imagen de ${performer.name}",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
            ) {
                Text(
                        text = "Descripcion",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                        text = performer.description,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
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
                text = "Error al cargar la información del artista",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}
