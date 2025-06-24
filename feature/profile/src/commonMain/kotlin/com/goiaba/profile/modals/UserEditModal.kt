package com.goiaba.profile.modals

import androidx.compose.foundation.background
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.goiaba.data.models.profile.strapiUser.StrapiUser
import com.goiaba.shared.FontSize
import com.goiaba.shared.Resources
import org.jetbrains.compose.resources.painterResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditModal(
    isVisible: Boolean,
    user: StrapiUser?,
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (
        username: String,
        dob: String
    ) -> Unit
) {
    val focusManager = LocalFocusManager.current
    
    // Form state
    var username by remember(user) { mutableStateOf(user?.username ?: "") }
    var dob by remember(user) { mutableStateOf(user?.profile?.dob ?: "") }
    
    // Date picker state
    var showDatePicker by remember { mutableStateOf(false) }
    
    // Parse initial date if available
    var initialSelectedDateMillis by remember { mutableStateOf<Long?>(null) }
    
    // Error states
    var usernameError by remember { mutableStateOf<String?>(null) }
    var dobError by remember { mutableStateOf<String?>(null) }
    
    // Parse initial date if available
    LaunchedEffect(user) {
        username = user?.username ?: ""
        dob = user?.profile?.dob ?: ""
        
        // Parse the date if it's in the correct format
        if (dob.matches(Regex("""^\d{4}-\d{2}-\d{2}$"""))) {
            try {
                val localDate = LocalDate.parse(dob)
                initialSelectedDateMillis = localDate.toEpochDay() * 24 * 60 * 60 * 1000
            } catch (e: Exception) {
                initialSelectedDateMillis = null
            }
        }
        
        // Clear errors
        usernameError = null
        dobError = null
    }
    
    fun validateForm(): Boolean {
        var isValid = true
        
        if (username.isBlank()) {
            usernameError = "Username is required"
            isValid = false
        } else if (username.length < 3) {
            usernameError = "Username must be at least 3 characters"
            isValid = false
        } else {
            usernameError = null
        }
        
        // Date validation (simple format check)
        if (dob.isNotBlank() && !isValidDate(dob)) {
            dobError = "Please use YYYY-MM-DD format"
            isValid = false
        } else {
            dobError = null
        }
        
        return isValid
    }
    
    if (isVisible && user != null) {
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
                        .fillMaxHeight(0.8f),
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
                                text = "Edit Profile",
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
                            // User information section
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
                                        text = "Account Information",
                                        fontSize = FontSize.MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    
                                    // Email (read-only)
                                    OutlinedTextField(
                                        value = user.email,
                                        onValueChange = { },
                                        label = { Text("Email") },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = false,
                                        readOnly = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Username
                                    OutlinedTextField(
                                        value = username,
                                        onValueChange = { 
                                            username = it
                                            usernameError = null
                                        },
                                        label = { Text("Username") },
                                        placeholder = { Text("Enter username") },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !isLoading,
                                        isError = usernameError != null,
                                        supportingText = usernameError?.let { error ->
                                            { Text(error, color = MaterialTheme.colorScheme.error) }
                                        },
                                        keyboardOptions = KeyboardOptions(
                                            capitalization = KeyboardCapitalization.None,
                                            imeAction = ImeAction.Next
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                        )
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            // Profile information section
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
                                        text = "Profile Information",
                                        fontSize = FontSize.MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    
                                    // Date of Birth with DatePicker
                                    OutlinedTextField(
                                        value = dob,
                                        onValueChange = { 
                                            dob = it
                                            dobError = null
                                        },
                                        label = { Text("Date of Birth") },
                                        placeholder = { Text("YYYY-MM-DD") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { showDatePicker = true },
                                        enabled = !isLoading,
                                        isError = dobError != null,
                                        readOnly = true, // Make it read-only since we're using the date picker
                                        trailingIcon = {
                                            IconButton(onClick = { showDatePicker = true }) {
                                                Icon(
                                                    painter = painterResource(Resources.Icon.Calendar),
                                                    contentDescription = "Select date",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        supportingText = dobError?.let { error ->
                                            { Text(error, color = MaterialTheme.colorScheme.error) }
                                        } ?: {
                                            Text(
                                                "Click to select date",
                                                fontSize = FontSize.SMALL,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Read-only information section
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
                                        text = "Account Details",
                                        fontSize = FontSize.MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    
                                    // User ID
                                    OutlinedTextField(
                                        value = user.id.toString(),
                                        onValueChange = { },
                                        label = { Text("User ID") },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = false,
                                        readOnly = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    )
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    // Role
                                    OutlinedTextField(
                                        value = user.role.name,
                                        onValueChange = { },
                                        label = { Text("Role") },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = false,
                                        readOnly = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    )
                                }
                            }
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
                                            username.trim(),
                                            dob.trim()
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
                                        Text("Saving...")
                                    }
                                } else {
                                    Text("Save Changes")
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialSelectedDateMillis
            )
            
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = java.time.Instant.ofEpochMilli(millis)
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate()
                                dob = date.format(DateTimeFormatter.ISO_LOCAL_DATE) // Format as YYYY-MM-DD
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = { Text("Select Date of Birth") },
                    headline = { Text("Please select your date of birth") },
                    showModeToggle = false,
                    dateValidator = { timestamp ->
                        // Validate that the selected date is not in the future
                        timestamp <= System.currentTimeMillis()
                    }
                )
            }
        }
    }
}

// Simple date validation function
private fun isValidDate(date: String): Boolean {
    // Basic format check for YYYY-MM-DD
    val regex = Regex("""^\d{4}-\d{2}-\d{2}$""")
    if (!regex.matches(date)) return false
    
    try {
        // Try to parse the date to validate it further
        LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
        return true
    } catch (e: Exception) {
        return false
    }
}