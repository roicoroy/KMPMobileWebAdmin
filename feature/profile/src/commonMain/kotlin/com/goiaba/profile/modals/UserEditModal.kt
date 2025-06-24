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
    var selectedYear by remember { mutableStateOf(2000) }
    var selectedMonth by remember { mutableStateOf(1) }
    var selectedDay by remember { mutableStateOf(1) }
    
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
                val parts = dob.split("-")
                if (parts.size == 3) {
                    selectedYear = parts[0].toInt()
                    selectedMonth = parts[1].toInt()
                    selectedDay = parts[2].toInt()
                }
            } catch (e: Exception) {
                // If parsing fails, use default values
                selectedYear = 2000
                selectedMonth = 1
                selectedDay = 1
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
        
        // Custom Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Format the date as YYYY-MM-DD
                            dob = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Date of Birth",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Year picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Year:",
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        // Simple number picker for year
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { 
                                    if (selectedYear > 1900) selectedYear--
                                }
                            ) {
                                Text("-", fontSize = FontSize.LARGE)
                            }
                            
                            Text(
                                text = selectedYear.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            
                            IconButton(
                                onClick = { 
                                    if (selectedYear < getCurrentYear()) selectedYear++
                                }
                            ) {
                                Text("+", fontSize = FontSize.LARGE)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Month picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Month:",
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { 
                                    if (selectedMonth > 1) selectedMonth--
                                    else {
                                        selectedMonth = 12
                                        if (selectedYear > 1900) selectedYear--
                                    }
                                }
                            ) {
                                Text("-", fontSize = FontSize.LARGE)
                            }
                            
                            Text(
                                text = selectedMonth.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            
                            IconButton(
                                onClick = { 
                                    if (selectedMonth < 12) selectedMonth++
                                    else {
                                        selectedMonth = 1
                                        if (selectedYear < getCurrentYear()) selectedYear++
                                    }
                                }
                            ) {
                                Text("+", fontSize = FontSize.LARGE)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Day picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Day:",
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { 
                                    if (selectedDay > 1) selectedDay--
                                    else {
                                        // Go to previous month's last day
                                        selectedMonth = if (selectedMonth > 1) selectedMonth - 1 else 12
                                        if (selectedMonth == 12 && selectedYear > 1900) selectedYear--
                                        selectedDay = getDaysInMonth(selectedMonth, selectedYear)
                                    }
                                }
                            ) {
                                Text("-", fontSize = FontSize.LARGE)
                            }
                            
                            Text(
                                text = selectedDay.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            
                            IconButton(
                                onClick = { 
                                    val daysInMonth = getDaysInMonth(selectedMonth, selectedYear)
                                    if (selectedDay < daysInMonth) selectedDay++
                                    else {
                                        // Go to next month's first day
                                        selectedDay = 1
                                        if (selectedMonth < 12) selectedMonth++
                                        else {
                                            selectedMonth = 1
                                            if (selectedYear < getCurrentYear()) selectedYear++
                                        }
                                    }
                                }
                            ) {
                                Text("+", fontSize = FontSize.LARGE)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Preview of selected date
                    Text(
                        text = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
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
        val parts = date.split("-")
        if (parts.size != 3) return false
        
        val year = parts[0].toInt()
        val month = parts[1].toInt()
        val day = parts[2].toInt()
        
        // Basic validation
        if (year < 1900 || year > getCurrentYear()) return false
        if (month < 1 || month > 12) return false
        
        val daysInMonth = getDaysInMonth(month, year)
        if (day < 1 || day > daysInMonth) return false
        
        return true
    } catch (e: Exception) {
        return false
    }
}

// Helper function to get current year
private fun getCurrentYear(): Int {
    return 2025 // Since we're in 2025 according to the system prompt
}

// Helper function to get days in a month
private fun getDaysInMonth(month: Int, year: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30 // Default fallback
    }
}

// Helper function to check if a year is a leap year
private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}