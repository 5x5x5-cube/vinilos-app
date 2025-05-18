package com.uniandes.vinilosapp.views.album

import android.app.Application
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uniandes.vinilosapp.models.GENRE
import com.uniandes.vinilosapp.viewmodels.CreateAlbumViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlbumScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as Application
    val context = LocalContext.current

    val viewModel: CreateAlbumViewModel =
            viewModel(factory = CreateAlbumViewModel.Factory(application))

    // Define a more rounded corner shape for all input fields
    val inputShape = RoundedCornerShape(16.dp)

    // Observe view model state
    val name by viewModel.name.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val coverUrl by viewModel.coverUrl.observeAsState("")
    val isValidUrl by viewModel.isValidUrl.observeAsState(false)
    val genre by viewModel.genre.observeAsState()
    val recordLabel by viewModel.recordLabel.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val createSuccess by viewModel.createSuccess.observeAsState(false)
    val isFormValid by viewModel.isFormValid.observeAsState(false)

    // Release date state
    val dateFormatted by viewModel.releaseDateFormatted.observeAsState("Seleccionar fecha")

    // Touch states
    val nameTouched by viewModel.nameTouched.observeAsState(false)
    val descriptionTouched by viewModel.descriptionTouched.observeAsState(false)
    val coverUrlTouched by viewModel.coverUrlTouched.observeAsState(false)
    val genreTouched by viewModel.genreTouched.observeAsState(false)
    val recordLabelTouched by viewModel.recordLabelTouched.observeAsState(false)
    val releaseDateTouched by viewModel.releaseDateTouched.observeAsState(false)

    // Validation error states
    val nameError by viewModel.nameError.observeAsState()
    val descriptionError by viewModel.descriptionError.observeAsState()
    val coverUrlError by viewModel.coverUrlError.observeAsState()
    val genreError by viewModel.genreError.observeAsState()
    val recordLabelError by viewModel.recordLabelError.observeAsState()
    val releaseDateError by viewModel.releaseDateError.observeAsState()

    // Dropdown state
    var genreExpanded by remember { mutableStateOf(false) }
    var recordLabelExpanded by remember { mutableStateOf(false) }

    // Setup date picker
    val calendar = Calendar.getInstance()
    val datePickerDialog =
            DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        viewModel.updateReleaseDate(calendar.time)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            )

    // Set max date to today to restrict future dates
    datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

    // Handle success or error
    LaunchedEffect(createSuccess, error) {
        if (createSuccess) {
            Toast.makeText(context, "Álbum creado exitosamente", Toast.LENGTH_SHORT).show()
            // Set a refresh flag in the backstack entry for AlbumsScreen to detect
            navController.previousBackStackEntry?.savedStateHandle?.set("refresh_albums", true)
            navController.navigateUp()
        }

        error?.let { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            viewModel.resetState()
        }
    }

    // Ensure the refresh flag is set when we return to the AlbumsScreen
    DisposableEffect(Unit) {
        onDispose {
            if (createSuccess) {
                navController.previousBackStackEntry?.savedStateHandle?.set("refresh_albums", true)
            }
        }
    }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Crear Album", fontWeight = FontWeight.Bold) },
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
                // Image placeholder
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(200.dp)
                                        .padding(vertical = 16.dp)
                                        .clip(inputShape)
                                        .background(Color.LightGray.copy(alpha = 0.3f))
                                        .border(
                                                width = 1.dp,
                                                color =
                                                        if (coverUrlTouched && coverUrlError != null
                                                        )
                                                                Color.Red
                                                        else Color.LightGray,
                                                shape = inputShape
                                        ),
                        contentAlignment = Alignment.Center
                ) {
                    if (isValidUrl) {
                        AsyncImage(
                                model =
                                        ImageRequest.Builder(context)
                                                .data(coverUrl)
                                                .crossfade(true)
                                                .build(),
                                contentDescription = "Cover Image",
                                modifier = Modifier.fillMaxSize().clip(inputShape)
                        )
                    } else {
                        Text(
                                text = "Vista previa de la imagen",
                                color =
                                        if (coverUrlTouched && coverUrlError != null) Color.Red
                                        else Color.Gray
                        )
                    }
                }

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

                // Description field with rounded corners
                OutlinedTextField(
                        value = description,
                        onValueChange = { viewModel.updateDescription(it) },
                        label = { Text("Descripción") },
                        isError = descriptionTouched && descriptionError != null,
                        supportingText = {
                            if (descriptionTouched && descriptionError != null) {
                                Text(text = descriptionError!!, color = Color.Red)
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

                // Cover URL field with rounded corners
                OutlinedTextField(
                        value = coverUrl,
                        onValueChange = { viewModel.updateCoverUrl(it) },
                        label = { Text("URL del Cover") },
                        isError = coverUrlTouched && coverUrlError != null,
                        supportingText = {
                            if (coverUrlTouched && coverUrlError != null) {
                                Text(text = coverUrlError!!, color = Color.Red)
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

                // Release Date field with date picker
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    OutlinedTextField(
                            value = dateFormatted,
                            onValueChange = { /* Read only field */},
                            label = { Text("Fecha de lanzamiento") },
                            readOnly = true,
                            isError = releaseDateTouched && releaseDateError != null,
                            supportingText = {
                                if (releaseDateTouched && releaseDateError != null) {
                                    Text(text = releaseDateError!!, color = Color.Red)
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
                            trailingIcon = {
                                Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = "Seleccionar fecha",
                                        tint =
                                                if (releaseDateTouched && releaseDateError != null)
                                                        Color.Red
                                                else Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                    )

                    // Transparent clickable overlay
                    Box(
                            modifier =
                                    Modifier.matchParentSize().clickable(
                                                    indication = null, // No ripple effect
                                                    interactionSource =
                                                            remember { MutableInteractionSource() }
                                            ) {
                                        viewModel.onReleaseDateTouched()
                                        datePickerDialog.show()
                                    }
                    )
                }

                // Genre dropdown with rounded corners
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    OutlinedTextField(
                            value = genre?.toString() ?: "Selecciona un género",
                            onValueChange = {},
                            label = { Text("Género") },
                            readOnly = true,
                            isError = genreTouched && genreError != null,
                            supportingText = {
                                if (genreTouched && genreError != null) {
                                    Text(text = genreError!!, color = Color.Red)
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
                            trailingIcon = {
                                Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown",
                                        tint =
                                                if (genreTouched && genreError != null) Color.Red
                                                else Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                    )

                    // Transparent clickable overlay
                    Box(
                            modifier =
                                    Modifier.matchParentSize().clickable(
                                                    indication = null, // No ripple effect
                                                    interactionSource =
                                                            remember { MutableInteractionSource() }
                                            ) {
                                        genreExpanded = true
                                        viewModel.onGenreTouched()
                                    }
                    )

                    DropdownMenu(
                            expanded = genreExpanded,
                            onDismissRequest = { genreExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        GENRE.values().forEach { genreOption ->
                            DropdownMenuItem(
                                    text = { Text(genreOption.toString()) },
                                    onClick = {
                                        viewModel.updateGenre(genreOption)
                                        genreExpanded = false
                                    }
                            )
                        }
                    }
                }

                // Record label dropdown with rounded corners
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    OutlinedTextField(
                            value = recordLabel?.toString() ?: "Selecciona una disquera",
                            onValueChange = {},
                            label = { Text("Disquera") },
                            readOnly = true,
                            isError = recordLabelTouched && recordLabelError != null,
                            supportingText = {
                                if (recordLabelTouched && recordLabelError != null) {
                                    Text(text = recordLabelError!!, color = Color.Red)
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
                            trailingIcon = {
                                Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown",
                                        tint =
                                                if (recordLabelTouched && recordLabelError != null)
                                                        Color.Red
                                                else Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                    )

                    // Transparent clickable overlay
                    Box(
                            modifier =
                                    Modifier.matchParentSize().clickable(
                                                    indication = null, // No ripple effect
                                                    interactionSource =
                                                            remember { MutableInteractionSource() }
                                            ) {
                                        recordLabelExpanded = true
                                        viewModel.onRecordLabelTouched()
                                    }
                    )

                    DropdownMenu(
                            expanded = recordLabelExpanded,
                            onDismissRequest = { recordLabelExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        com.uniandes.vinilosapp.models.RECORD_LABEL.values().forEach { labelOption
                            ->
                            DropdownMenuItem(
                                    text = { Text(labelOption.toString()) },
                                    onClick = {
                                        viewModel.updateRecordLabel(labelOption)
                                        recordLabelExpanded = false
                                    }
                            )
                        }
                    }
                }

                // Save button with rounded corners
                Button(
                        onClick = {
                            // Mark all fields as touched when trying to save
                            viewModel.onNameTouched()
                            viewModel.onDescriptionTouched()
                            viewModel.onCoverUrlTouched()
                            viewModel.onGenreTouched()
                            viewModel.onRecordLabelTouched()
                            viewModel.onReleaseDateTouched()
                            viewModel.createAlbum()
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
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.Black,
                                ),
                ) { Text("Cancelar") }
            }
        }
    }
}
