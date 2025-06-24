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
    
    // Error states
    var usernameError by remember { mutableStateOf<String?>(null) }
    var dobError by remember { mutableStateOf<String?>(null) }
    
    // Reset form when user changes
    LaunchedEffect(user) {
        username = user?.username ?: ""
        dob = user?.profile?.dob ?: ""
        
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
                                    
                                    // Date of Birth
                                    OutlinedTextField(
                                        value = dob,
                                        onValueChange = { 
                                            dob = it
                                            dobError = null
                                        },
                                        label = { Text("Date of Birth") },
                                        placeholder = { Text("YYYY-MM-DD") },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !isLoading,
                                        isError = dobError != null,
                                        supportingText = dobError?.let { error ->
                                            { Text(error, color = MaterialTheme.colorScheme.error) }
                                        } ?: {
                                            Text(
                                                "Format: YYYY-MM-DD (e.g., 2000-01-31)",
                                                fontSize = FontSize.SMALL,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = { focusManager.clearFocus() }
                                        )
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
    }
}

// Simple date validation function
private fun isValidDate(date: String): Boolean {
    // Basic format check for YYYY-MM-DD
    val regex = Regex("""^\d{4}-\d{2}-\d{2}$""")
    if (!regex.matches(date)) return false
    
    // Additional validation could be added here
    return true
}