package com.goiaba.profile.modals

import androidx.compose.foundation.background
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.goiaba.data.models.adverts.CategoryResponse
import com.goiaba.data.models.profile.Advert
import com.goiaba.shared.FontSize
import com.goiaba.shared.Resources
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertEditModal(
    isVisible: Boolean,
    advert: Advert?,
    categories: List<CategoryResponse.Category>,
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        description: String,
        categoryId: String?,
        slug: String?
    ) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    
    // Form state
    var title by remember(advert) { mutableStateOf(advert?.title ?: "") }
    var description by remember(advert) { mutableStateOf(advert?.description ?: "") }
    var selectedCategory by remember(advert) { mutableStateOf<CategoryResponse.Category?>(null) }
    var slug by remember(advert) { mutableStateOf(advert?.slug ?: "") }
    var expanded by remember { mutableStateOf(false) }
    
    // Error states
    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    
    // Reset form when advert changes
    LaunchedEffect(advert) {
        title = advert?.title ?: ""
        description = advert?.description ?: ""
        slug = advert?.slug ?: ""
        titleError = null
        descriptionError = null
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
        
        return isValid
    }
    
    if (isVisible && advert != null) {
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
                                text = "Edit Advert",
                                fontSize = FontSize.LARGE,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Row {
                                if (onDelete != null) {
                                    TextButton(
                                        onClick = onDelete,
                                        enabled = !isLoading,
                                        colors = ButtonDefaults.textButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text("🗑️ Delete")
                                    }
                                }
                                
                                IconButton(onClick = onDismiss) {
                                    Icon(
                                        painter = painterResource(Resources.Icon.Close),
                                        contentDescription = "Close",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Form content
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Title field
                            OutlinedTextField(
                                value = title,
                                onValueChange = { 
                                    title = it
                                    titleError = null
                                },
                                label = { Text("Title") },
                                placeholder = { Text("Enter advert title") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading,
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
                                enabled = !isLoading,
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
                            
                            // Category dropdown (optional for editing)
                            if (categories.isNotEmpty()) {
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded && !isLoading }
                                ) {
                                    OutlinedTextField(
                                        value = selectedCategory?.name ?: "No category selected",
                                        onValueChange = { },
                                        readOnly = true,
                                        label = { Text("Category (Optional)") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        enabled = !isLoading,
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                        }
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        // Option to clear selection
                                        DropdownMenuItem(
                                            text = { Text("No category") },
                                            onClick = {
                                                selectedCategory = null
                                                expanded = false
                                            }
                                        )
                                        
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
                                                    selectedCategory = category
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            
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
                                enabled = !isLoading,
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
                                enabled = !isLoading
                            ) {
                                Text("Cancel")
                            }
                            
                            Button(
                                onClick = {
                                    if (validateForm()) {
                                        onSave(
                                            title.trim(),
                                            description.trim(),
                                            selectedCategory?.documentId,
                                            slug.takeIf { it.isNotBlank() }
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading
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
                                        Text("Updating...")
                                    }
                                } else {
                                    Text("Update Advert")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}