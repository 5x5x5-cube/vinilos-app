package com.uniandes.vinilosapp.views.album

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.uniandes.vinilosapp.viewmodels.CreateTrackViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrackScreen(albumId: Int, albumName: String, navController: NavController) {
    val application = LocalContext.current.applicationContext as Application
    val context = LocalContext.current

    val viewModel: CreateTrackViewModel =
            viewModel(factory = CreateTrackViewModel.Factory(application))

    // Set the album ID
    LaunchedEffect(albumId) { viewModel.setAlbum(albumId, albumName) }

    // Define a more rounded corner shape for all input fields
    val inputShape = RoundedCornerShape(16.dp)

    // Observe view model state
    val name by viewModel.name.observeAsState("")
    val duration by viewModel.duration.observeAsState("")
    val albumDisplay by viewModel.albumName.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val createSuccess by viewModel.createSuccess.observeAsState(false)
    val isFormValid by viewModel.isFormValid.observeAsState(false)

    // Touch states
    val nameTouched by viewModel.nameTouched.observeAsState(false)
    val durationTouched by viewModel.durationTouched.observeAsState(false)

    // Validation error states
    val nameError by viewModel.nameError.observeAsState()
    val durationError by viewModel.durationError.observeAsState()

    // Handle success or error
    LaunchedEffect(createSuccess, error) {
        if (createSuccess) {
            Toast.makeText(context, "Track añadido exitosamente", Toast.LENGTH_SHORT).show()
            // Set a refresh flag in the backstack entry for AlbumDetailScreen to detect
            navController.previousBackStackEntry?.savedStateHandle?.set("refresh_album", true)
            navController.navigateUp()
        }

        error?.let { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            viewModel.resetState()
        }
    }

    // Ensure the refresh flag is set when we return to the AlbumDetailScreen
    DisposableEffect(Unit) {
        onDispose {
            if (createSuccess) {
                navController.previousBackStackEntry?.savedStateHandle?.set("refresh_album", true)
            }
        }
    }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Agregar Track", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                        contentDescription = "Go back",
                                )
                            }
                        }
                )
            }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            Column(
                    modifier =
                            Modifier.fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(horizontal = 16.dp)
                                    .verticalScroll(rememberScrollState())
            ) {
                // Album selection field (readonly/disabled)
                OutlinedTextField(
                        value = albumDisplay,
                        onValueChange = { /* Read only field */},
                        label = { Text("Album") },
                        readOnly = true,
                        enabled = false,
                        shape = inputShape,
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Black,
                                        focusedLabelColor = Color.Black,
                                        unfocusedBorderColor = Color.Gray,
                                        disabledBorderColor = Color.Gray,
                                        disabledTextColor = Color.Gray
                                ),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                // Name field with rounded corners
                OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text("Nombre") },
                        isError = nameTouched && nameError != null,
                        supportingText = {
                            if (nameTouched && nameError != null) {
                                Text(text = nameError!!, color = Color.Red)
                            }
                        },
                        shape = inputShape,
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Black,
                                        focusedLabelColor = Color.Black,
                                        unfocusedBorderColor = Color.Gray,
                                        errorBorderColor = Color.Red,
                                ),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                // Duration field with rounded corners and pattern validation for mm:ss
                OutlinedTextField(
                        value = duration,
                        onValueChange = { newValue ->
                            // Only allow valid input pattern for mm:ss format
                            if (newValue.isEmpty() ||
                                            newValue.matches(Regex("^\\d{0,2}(:\\d{0,2})?$"))
                            ) {
                                viewModel.updateDuration(newValue)
                            }
                        },
                        label = { Text("Duración") },
                        isError = durationTouched && durationError != null,
                        supportingText = {
                            if (durationTouched && durationError != null) {
                                Text(text = durationError!!, color = Color.Red)
                            } else {
                                Text(text = "Formato: mm:ss (ej. 3:45)")
                            }
                        },
                        shape = inputShape,
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Black,
                                        focusedLabelColor = Color.Black,
                                        unfocusedBorderColor = Color.Gray,
                                        errorBorderColor = Color.Red
                                ),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                // Save button with rounded corners
                Button(
                        onClick = {
                            // Mark all fields as touched when trying to save
                            viewModel.onNameTouched()
                            viewModel.onDurationTouched()
                            viewModel.createTrack()
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.Gray
                                ),
                        enabled = isFormValid
                ) { Text("Guardar") }

                // Cancel button with matching rounded corners
                TextButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
                ) { Text("Cancelar") }
            }
        }
    }
}
