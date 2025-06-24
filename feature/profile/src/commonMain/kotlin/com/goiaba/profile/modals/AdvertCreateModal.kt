package com.goiaba.profile.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.goiaba.data.models.adverts.CategoryResponse
import com.goiaba.data.services.logger.UploadedImage
import com.goiaba.shared.FontSize
import com.goiaba.shared.Resources
import com.goiaba.shared.util.ImagePickerResult
import com.goiaba.shared.util.rememberImagePicker
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertCreateModal(
    isVisible: Boolean,
    categories: List<CategoryResponse.Category>,
    uploadedImages: List<UploadedImage> = emptyList(),
    isLoading: Boolean = false,
    isUploadingImage: Boolean = false,
    onImageUpload: (ByteArray, String) -> Unit,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        description: String,
        categoryId: String,
        coverId: String?,
        slug: String?
    ) -> Unit
) {
    val focusManager = LocalFocusManager.current
    
    // Form state
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    var selectedCoverId by remember { mutableStateOf<String?>(null) }
    var slug by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var coverExpanded by remember { mutableStateOf(false) }
    
    // Error states
    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }
    
    // Image picker state
    var shouldPickImage by remember { mutableStateOf(false) }
    var selectedImageData by remember { mutableStateOf<ByteArray?>(null) }
    var selectedImageName by remember { mutableStateOf<String?>(null) }
    val imagePicker = rememberImagePicker()
    
    // Reset form when modal opens/closes
    LaunchedEffect(isVisible) {
        if (isVisible) {
            title = ""
            description = ""
            selectedCategoryId = null
            selectedCoverId = null
            slug = ""
            titleError = null
            descriptionError = null
            categoryError = null
            selectedImageData = null
            selectedImageName = null
        }
    }
    
    // Handle image picking
    LaunchedEffect(shouldPickImage) {
        if (shouldPickImage) {
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
                shouldPickImage = false
            }
        }
    }
    
    fun validateForm(): Boolean {
        var isValid = true
        
        if (title.isBlank()) {
            titleError = "Title is required"
            isValid = false
        } else {
            titleError = null
        }
        
        if (description.isBlank()) {
            descriptionError = "Description is required"
            isValid = false
        } else {
            descriptionError = null
        }
        
        if (selectedCategoryId == null) {
            categoryError = "Please select a category"
            isValid = false
        } else {
            categoryError = null
        }
        
        return isValid
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
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.9f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Create New Advert",
                                fontSize = FontSize.LARGE,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Close),
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Form content
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Image upload section
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Cover Image",
                                        fontSize = FontSize.MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    
                                    // Image preview or placeholder
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable(enabled = !isLoading && !isUploadingImage) { 
                                                shouldPickImage = true 
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (selectedCoverId != null && uploadedImages.isNotEmpty()) {
                                            // Find the selected image
                                            val selectedImage = uploadedImages.find { it.id.toString() == selectedCoverId }
                                            if (selectedImage != null) {
                                                AsyncImage(
                                                    model = selectedImage.url,
                                                    contentDescription = "Selected cover image",
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                            } else {
                                                // Placeholder if image not found
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Icon(
                                                        painter = painterResource(Resources.Icon.Plus),
                                                        contentDescription = "Add image",
                                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        modifier = Modifier.size(40.dp)
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = "Click to upload image",
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        } else {
                                            // Default placeholder
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                if (isUploadingImage) {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier.size(40.dp),
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = "Uploading image...",
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                } else {
                                                    Icon(
                                                        painter = painterResource(Resources.Icon.Plus),
                                                        contentDescription = "Add image",
                                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        modifier = Modifier.size(40.dp)
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = "Click to upload image",
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    // Select from existing images
                                    if (uploadedImages.isNotEmpty()) {
                                        ExposedDropdownMenuBox(
                                            expanded = coverExpanded,
                                            onExpandedChange = { coverExpanded = !coverExpanded && !isLoading && !isUploadingImage }
                                        ) {
                                            OutlinedTextField(
                                                value = selectedCoverId?.let { id ->
                                                    uploadedImages.find { it.id.toString() == id }?.name ?: "Select image"
                                                } ?: "Select existing image",
                                                onValueChange = { },
                                                readOnly = true,
                                                label = { Text("Cover Image") },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .menuAnchor(),
                                                enabled = !isLoading && !isUploadingImage,
                                                trailingIcon = {
                                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = coverExpanded)
                                                }
                                            )
                                            
                                            ExposedDropdownMenu(
                                                expanded = coverExpanded,
                                                onDismissRequest = { coverExpanded = false }
                                            ) {
                                                // Option to clear selection
                                                DropdownMenuItem(
                                                    text = { Text("No image") },
                                                    onClick = {
                                                        selectedCoverId = null
                                                        coverExpanded = false
                                                    }
                                                )
                                                
                                                uploadedImages.forEach { image ->
                                                    DropdownMenuItem(
                                                        text = { Text(image.name) },
                                                        onClick = {
                                                            selectedCoverId = image.id.toString()
                                                            coverExpanded = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            
                            // Title field
                            OutlinedTextField(
                                value = title,
                                onValueChange = { 
                                    title = it
                                    titleError = null
                                    // Auto-generate slug from title
                                    slug = it.lowercase()
                                        .replace(Regex("[^a-z0-9\\s]"), "")
                                        .replace(Regex("\\s+"), "-")
                                        .trim('-')
                                },
                                label = { Text("Title") },
                                placeholder = { Text("Enter advert title") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading && !isUploadingImage,
                                isError = titleError != null,
                                supportingText = titleError?.let { error ->
                                    { Text(error, color = MaterialTheme.colorScheme.error) }
                                },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Description field
                            OutlinedTextField(
                                value = description,
                                onValueChange = { 
                                    description = it
                                    descriptionError = null
                                },
                                label = { Text("Description") },
                                placeholder = { Text("Enter advert description") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading && !isUploadingImage,
                                isError = descriptionError != null,
                                supportingText = descriptionError?.let { error ->
                                    { Text(error, color = MaterialTheme.colorScheme.error) }
                                },
                                minLines = 3,
                                maxLines = 5,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Category dropdown
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded && !isLoading && !isUploadingImage }
                            ) {
                                OutlinedTextField(
                                    value = selectedCategoryId ?: "",
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Category") },
                                    placeholder = { Text("Select a category") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    enabled = !isLoading && !isUploadingImage,
                                    isError = categoryError != null,
                                    supportingText = categoryError?.let { error ->
                                        { Text(error, color = MaterialTheme.colorScheme.error) }
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                    }
                                )
                                
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    categories.forEach { category ->
                                        DropdownMenuItem(
                                            text = { 
                                                Column {
                                                    Text(
                                                        text = category.name,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    if (category.description.isNotBlank()) {
                                                        Text(
                                                            text = category.description,
                                                            fontSize = FontSize.SMALL,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                            },
                                            onClick = {
                                                selectedCategoryId = category.id.toString()
                                                categoryError = null
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Slug field (optional)
                            OutlinedTextField(
                                value = slug,
                                onValueChange = { 
                                    slug = it.lowercase()
                                        .replace(Regex("[^a-z0-9\\-]"), "")
                                },
                                label = { Text("URL Slug (Optional)") },
                                placeholder = { Text("auto-generated-from-title") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading && !isUploadingImage,
                                supportingText = {
                                    Text(
                                        text = "Leave empty to auto-generate from title",
                                        fontSize = FontSize.SMALL,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Uri,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading && !isUploadingImage
                            ) {
                                Text("Cancel")
                            }
                            
                            Button(
                                onClick = {
                                    if (validateForm()) {
                                        onSave(
                                            title.trim(),
                                            description.trim(),
                                            selectedCategoryId.toString(),
                                            selectedCoverId,
                                            slug.takeIf { it.isNotBlank() }
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading && !isUploadingImage
                            ) {
                                if (isLoading || isUploadingImage) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Text(if (isUploadingImage) "Uploading..." else "Creating...")
                                    }
                                } else {
                                    Text("Create Advert")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}