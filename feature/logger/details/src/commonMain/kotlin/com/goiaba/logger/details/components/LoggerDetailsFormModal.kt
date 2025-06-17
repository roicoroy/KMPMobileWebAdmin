package com.goiaba.logger.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.goiaba.shared.FontSize
import com.goiaba.shared.util.ImagePicker
import com.goiaba.shared.util.ImagePickerResult
import com.goiaba.shared.util.rememberImagePicker
import kotlinx.coroutines.launch
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggerDetailsFormModal(
    isVisible: Boolean,
    isEditMode: Boolean = false,
    initialCowName: String = "",
    initialDate: String = "",
    initialImageId: Int = 1,
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onSubmit: (String, String, Int) -> Unit,
    onImageUpload: (ByteArray, String) -> Unit = { _, _ -> }
) {
    var cowName by remember(initialCowName) { mutableStateOf(initialCowName) }
    var selectedDate by remember(initialDate) {
        mutableStateOf(
            if (initialDate.isNotEmpty()) {
                try {
                    // Parse date from "2025-06-05" format
                    LocalDate.parse(initialDate.take(10))
                } catch (e: Exception) {
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                }
            } else {
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            }
        )
    }
    var selectedImageId by remember(initialImageId) { mutableStateOf(initialImageId) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedImageData by remember { mutableStateOf<ByteArray?>(null) }
    var selectedImageName by remember { mutableStateOf<String?>(null) }
    var isUploadingImage by remember { mutableStateOf(false) }
    var shouldPickImage by remember { mutableStateOf(false) }

    // Form validation states
    var cowNameError by remember { mutableStateOf<String?>(null) }

    // Image picker
    val imagePicker = rememberImagePicker()
    val scope = rememberCoroutineScope()

    // Handle image picking
    LaunchedEffect(shouldPickImage) {
        if (shouldPickImage) {
            isUploadingImage = true
            try {
                val result = imagePicker.pickImage()
                when (result) {
                    is ImagePickerResult.Success -> {
                        selectedImageData = result.imageData
                        selectedImageName = result.fileName

                        // Save to gallery first
                        val saved = imagePicker.saveImageToGallery(
                            result.imageData,
                            result.fileName
                        )

                        if (saved) {
                            // Upload to Strapi
                            onImageUpload(result.imageData, result.fileName)
                        }
                    }

                    is ImagePickerResult.Error -> {
                        // Handle error - could show a snackbar or error message
                    }

                    null -> {
                        // User cancelled
                    }
                }
            } catch (e: Exception) {
                // Handle exception
            } finally {
                isUploadingImage = false
                shouldPickImage = false
            }
        }
    }

    // Reset form when modal becomes visible
    LaunchedEffect(isVisible, isEditMode) {
        if (isVisible) {
            cowName = initialCowName
            selectedImageId = initialImageId
            selectedImageData = null
            selectedImageName = null
            if (initialDate.isNotEmpty()) {
                try {
                    selectedDate = LocalDate.parse(initialDate.take(10))
                } catch (e: Exception) {
                    selectedDate =
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                }
            }
            cowNameError = null
        }
    }

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header
                        Text(
                            text = if (isEditMode) "🐄 Edit Cow Logger" else "🐄 Add New Cow Logger",
                            fontSize = FontSize.LARGE,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Cow Name Input Field
                        OutlinedTextField(
                            value = cowName,
                            onValueChange = {
                                cowName = it
                                cowNameError = null
                            },
                            label = { Text("Cow Name") },
                            placeholder = { Text("Enter cow name...") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading && !isUploadingImage,
                            isError = cowNameError != null,
                            supportingText = cowNameError?.let { error ->
                                {
                                    Text(
                                        text = error,
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = FontSize.SMALL
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Date Picker Field
                        OutlinedTextField(
                            value = selectedDate.toString(),
                            onValueChange = { },
                            label = { Text("Date") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading && !isUploadingImage,
                            readOnly = true,
                            trailingIcon = {
                                IconButton(
                                    onClick = { showDatePicker = true },
                                    enabled = !isLoading && !isUploadingImage
                                ) {
                                    Text(
                                        text = "📅",
                                        fontSize = FontSize.MEDIUM
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Image Selection Section
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Image Selection",
                                    fontSize = FontSize.MEDIUM,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Image picker button
                                Button(
                                    onClick = {
                                        shouldPickImage = true
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !isLoading && !isUploadingImage,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    if (isUploadingImage) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                strokeWidth = 2.dp,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                            Text("Uploading...")
                                        }
                                    } else {
                                        Text("📷 Pick Image from Gallery")
                                    }
                                }

                                // Show selected image info
                                selectedImageName?.let { name ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Selected: $name",
                                        fontSize = FontSize.SMALL,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Fallback: Image ID dropdown (for existing images)
                                var expanded by remember { mutableStateOf(false) }

                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = {
                                        expanded = !expanded && !isLoading && !isUploadingImage
                                    }
                                ) {
                                    OutlinedTextField(
                                        value = "Image ID: $selectedImageId",
                                        onValueChange = { },
                                        label = { Text("Or Select Existing Image") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        enabled = !isLoading && !isUploadingImage,
                                        readOnly = true,
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )

                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        // Sample image options (in real app, this would come from API)
                                        (1..5).forEach { imageId ->
                                            DropdownMenuItem(
                                                text = { Text("Image ID: $imageId") },
                                                onClick = {
                                                    selectedImageId = imageId
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Cancel Button
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading && !isUploadingImage,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Cancel",
                                    fontSize = FontSize.REGULAR,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Submit Button
                            Button(
                                onClick = {
                                    when {
                                        cowName.isBlank() -> {
                                            cowNameError = "Cow name cannot be empty"
                                        }

                                        cowName.length < 2 -> {
                                            cowNameError = "Cow name must be at least 2 characters"
                                        }

                                        else -> {
                                            // Format date as required by API: "2022-12-27 08:26:49.219717"
                                            val formattedDate = "${selectedDate} 08:26:49.219717"
                                            onSubmit(cowName.trim(), formattedDate, selectedImageId)
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading && !isUploadingImage,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                if (isLoading) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Text(
                                            text = if (isEditMode) "Updating..." else "Creating...",
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } else {
                                    Text(
                                        text = if (isEditMode) "🐄 Update Logger" else "🐄 Create Logger",
                                        fontSize = FontSize.REGULAR,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.toEpochDays() * 24 * 60 * 60 * 1000L
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.fromEpochMilliseconds(millis)
                        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                        onDateSelected(localDate)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = "Select Date",
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }
}