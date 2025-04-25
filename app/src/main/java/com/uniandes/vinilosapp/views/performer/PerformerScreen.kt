package com.uniandes.vinilosapp.views.performer

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.uniandes.vinilosapp.viewmodels.PerformerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformerScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel: PerformerViewModel = viewModel(factory = PerformerViewModel.Factory(application))
    val performers by viewModel.performers.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artistas", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(performers) { performer ->
                PerformerRow(
                    performer = performer
                )
                Divider()
            }
        }
    }
}