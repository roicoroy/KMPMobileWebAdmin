package com.goiaba.profile.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.goiaba.data.models.profile.Addresse
import com.goiaba.profile.components.AddressEditModal
import com.goiaba.profile.components.DetailRow
import com.goiaba.profile.utils.formatDate
import com.goiaba.shared.*
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressDetailsModal(
    isVisible: Boolean,
    address: Addresse?,
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onUpdateAddress: (
        firstName: String,
        lastName: String,
        firstLineAddress: String,
        secondLineAddress: String?,
        postCode: String,
        city: String?,
        country: String?,
        phoneNumber: String?
    ) -> Unit,
    onDeleteAddress: () -> Unit
) {
    var showEditModal by remember { mutableStateOf(false) }

    if (isVisible && address != null) {
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
                                text = "Address Details",
                                fontSize = FontSize.LARGE,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Row {
                                IconButton(
                                    onClick = { showEditModal = true },
                                    enabled = !isLoading
                                ) {
                                    Icon(
                                        painter = painterResource(Resources.Icon.Edit),
                                        contentDescription = "Edit address",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
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
                        
                        // Content
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Main Address Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp)
                                ) {
                                    // Header with ID Badge
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = RoundedCornerShape(20.dp)
                                        ) {
                                            Text(
                                                text = "🏠 ID: ${address.id}",
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                fontSize = FontSize.SMALL,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                        
                                        Surface(
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shape = RoundedCornerShape(20.dp)
                                        ) {
                                            Text(
                                                text = "✅ Active",
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                fontSize = FontSize.SMALL,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(20.dp))
                                    
                                    // Full Name
                                    Text(
                                        text = "${address.firstName} ${address.lastName}",
                                        fontSize = FontSize.EXTRA_LARGE,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    
                                    Spacer(modifier = Modifier.height(20.dp))
                                    
                                    // Address Information
                                    DetailRow(
                                        icon = "🏠",
                                        label = "Street Address",
                                        value = buildString {
                                            append(address.firstLineAddress)
                                            address.secondLineAddress?.let { 
                                                if (it.isNotBlank()) {
                                                    append("\n$it")
                                                }
                                            }
                                        }
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            DetailRow(
                                                icon = "🏙️",
                                                label = "City",
                                                value = address.city ?: "Not specified"
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            DetailRow(
                                                icon = "📮",
                                                label = "Postal Code",
                                                value = address.postCode
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    DetailRow(
                                        icon = "🌍",
                                        label = "Country",
                                        value = address.country ?: "Not specified"
                                    )
                                    
                                    // Phone Number (if available)
                                    address.phoneNumber?.let { phone ->
                                        if (phone.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            DetailRow(
                                                icon = "📞",
                                                label = "Phone Number",
                                                value = phone
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Document ID
                                    DetailRow(
                                        icon = "📄",
                                        label = "Document ID",
                                        value = address.documentId
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Timestamps Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = "Address History",
                                        fontSize = FontSize.MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    
                                    DetailRow(
                                        icon = "📅",
                                        label = "Created",
                                        value = formatDate(address.createdAt)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    DetailRow(
                                        icon = "🔄",
                                        label = "Last Updated",
                                        value = formatDate(address.updatedAt)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    DetailRow(
                                        icon = "📤",
                                        label = "Published",
                                        value = formatDate(address.publishedAt)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Close")
                            }
                            
                            Button(
                                onClick = { showEditModal = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("✏️ Edit Address")
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Edit Modal
        AddressEditModal(
            isVisible = showEditModal,
            address = address,
            isLoading = isLoading,
            onDismiss = { showEditModal = false },
            onSave = { firstName, lastName, firstLineAddress, secondLineAddress, postCode, city, country, phoneNumber ->
                onUpdateAddress(firstName, lastName, firstLineAddress, secondLineAddress, postCode, city, country, phoneNumber)
                showEditModal = false
            },
            onDelete = {
                onDeleteAddress()
                showEditModal = false
            }
        )
    }
}