package com.uniandes.vinilosapp.views.collector

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
import com.uniandes.vinilosapp.viewmodels.CollectorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectorScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as Application
    val viewModel: CollectorViewModel = viewModel(factory = CollectorViewModel.Factory(application))
    val collectors by viewModel.collectors.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coleccionistas", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(collectors) { collector ->
                CollectorRow(
                    collector = collector,
                    onVerClick = {
                        navController.navigate(
                            "collectors/${collector.collectorID}"
                        )
                    }
                )
                Divider()
            }
        }
    }
}