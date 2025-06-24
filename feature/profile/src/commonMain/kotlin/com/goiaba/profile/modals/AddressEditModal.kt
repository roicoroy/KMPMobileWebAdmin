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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.goiaba.shared.FontSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressEditModal(
    isVisible: Boolean,
    address: com.goiaba.data.models.profile.strapiUser.StrapiProfile.Data.Addresse?,
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (
        firstName: String,
        lastName: String,
        firstLineAddress: String,
        secondLineAddress: String?,
        postCode: String,
        city: String?,
        country: String?,
        phoneNumber: String?
    ) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    
    // Form state
    var firstName by remember(address) { mutableStateOf(address?.firstName ?: "") }
    var lastName by remember(address) { mutableStateOf(address?.lastName ?: "") }
    var firstLineAddress by remember(address) { mutableStateOf(address?.firstLineAddress ?: "") }
    var secondLineAddress by remember(address) { mutableStateOf(address?.secondLineAddress ?: "") }
    var postCode by remember(address) { mutableStateOf(address?.postCode ?: "") }
    var city by remember(address) { mutableStateOf(address?.city ?: "") }
    var country by remember(address) { mutableStateOf(address?.country ?: "") }
    var phoneNumber by remember(address) { mutableStateOf(address?.phoneNumber ?: "") }
    
    // Error states
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var firstLineAddressError by remember { mutableStateOf<String?>(null) }
    var postCodeError by remember { mutableStateOf<String?>(null) }
    
    // Reset form when address changes
    LaunchedEffect(address) {
        firstName = address?.firstName ?: ""
        lastName = address?.lastName ?: ""
        firstLineAddress = address?.firstLineAddress ?: ""
        secondLineAddress = address?.secondLineAddress ?: ""
        postCode = address?.postCode ?: ""
        city = address?.city ?: ""
        country = address?.country ?: ""
        phoneNumber = address?.phoneNumber ?: ""
        
        // Clear errors
        firstNameError = null
        lastNameError = null
        firstLineAddressError = null
        postCodeError = null
    }
    
    fun validateForm(): Boolean {
        var isValid = true
        
        if (firstName.isBlank()) {
            firstNameError = "First name is required"
            isValid = false
        } else {
            firstNameError = null
        }
        
        if (lastName.isBlank()) {
            lastNameError = "Last name is required"
            isValid = false
        } else {
            lastNameError = null
        }
        
        if (firstLineAddress.isBlank()) {
            firstLineAddressError = "Address is required"
            isValid = false
        } else {
            firstLineAddressError = null
        }
        
        if (postCode.isBlank()) {
            postCodeError = "Postal code is required"
            isValid = false
        } else {
            postCodeError = null
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
                                text = if (address != null) "Edit Address" else "Add Address",
                                fontSize = FontSize.LARGE,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            if (address != null && onDelete != null) {
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
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Form content
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Name fields
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = firstName,
                                    onValueChange = { 
                                        firstName = it
                                        firstNameError = null
                                    },
                                    label = { Text("First Name") },
                                    modifier = Modifier.weight(1f),
                                    enabled = !isLoading,
                                    isError = firstNameError != null,
                                    supportingText = firstNameError?.let { error ->
                                        { Text(error, color = MaterialTheme.colorScheme.error) }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                    )
                                )
                                
                                OutlinedTextField(
                                    value = lastName,
                                    onValueChange = { 
                                        lastName = it
                                        lastNameError = null
                                    },
                                    label = { Text("Last Name") },
                                    modifier = Modifier.weight(1f),
                                    enabled = !isLoading,
                                    isError = lastNameError != null,
                                    supportingText = lastNameError?.let { error ->
                                        { Text(error, color = MaterialTheme.colorScheme.error) }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                    )
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Address fields
                            OutlinedTextField(
                                value = firstLineAddress,
                                onValueChange = { 
                                    firstLineAddress = it
                                    firstLineAddressError = null
                                },
                                label = { Text("Address Line 1") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading,
                                isError = firstLineAddressError != null,
                                supportingText = firstLineAddressError?.let { error ->
                                    { Text(error, color = MaterialTheme.colorScheme.error) }
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = secondLineAddress,
                                onValueChange = { secondLineAddress = it },
                                label = { Text("Address Line 2 (Optional)") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // City and Postal Code
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = city,
                                    onValueChange = { city = it },
                                    label = { Text("City") },
                                    modifier = Modifier.weight(1f),
                                    enabled = !isLoading,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                    )
                                )
                                
                                OutlinedTextField(
                                    value = postCode,
                                    onValueChange = { 
                                        postCode = it
                                        postCodeError = null
                                    },
                                    label = { Text("Postal Code") },
                                    modifier = Modifier.weight(1f),
                                    enabled = !isLoading,
                                    isError = postCodeError != null,
                                    supportingText = postCodeError?.let { error ->
                                        { Text(error, color = MaterialTheme.colorScheme.error) }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                    )
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = country,
                                onValueChange = { country = it },
                                label = { Text("Country") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                )
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Phone Number (Optional)") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
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
                                            firstName.trim(),
                                            lastName.trim(),
                                            firstLineAddress.trim(),
                                            secondLineAddress.takeIf { it.isNotBlank() },
                                            postCode.trim(),
                                            city.takeIf { it.isNotBlank() },
                                            country.takeIf { it.isNotBlank() },
                                            phoneNumber.takeIf { it.isNotBlank() }
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
                                    Text(if (address != null) "Update" else "Save")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}