package com.goiaba.profile.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.data.models.profile.Addresse
import com.goiaba.profile.components.AddressEditModal
import com.goiaba.shared.*
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressDetailsScreen(
    address: Addresse,
    isLoading: Boolean = false,
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
    onDeleteAddress: () -> Unit,
    navigateBack: () -> Unit
) {
    var showEditModal by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Address Details",
                            fontSize = FontSize.LARGE,
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                painter = painterResource(Resources.Icon.BackArrow),
                                contentDescription = "Back",
                                tint = IconPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { showEditModal = true },
                            enabled = !isLoading
                        ) {
                            Icon(
                                painter = painterResource(Resources.Icon.Edit),
                                contentDescription = "Edit address",
                                tint = IconPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
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
                        AddressDetailRow(
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
                                AddressDetailRow(
                                    icon = "🏙️",
                                    label = "City",
                                    value = address.city ?: "Not specified"
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                AddressDetailRow(
                                    icon = "📮",
                                    label = "Postal Code",
                                    value = address.postCode
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        AddressDetailRow(
                            icon = "🌍",
                            label = "Country",
                            value = address.country ?: "Not specified"
                        )
                        
                        // Phone Number (if available)
                        address.phoneNumber?.let { phone ->
                            if (phone.isNotBlank()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                AddressDetailRow(
                                    icon = "📞",
                                    label = "Phone Number",
                                    value = phone
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Document ID
                        AddressDetailRow(
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
                        
                        AddressDetailRow(
                            icon = "📅",
                            label = "Created",
                            value = formatDate(address.createdAt)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        AddressDetailRow(
                            icon = "🔄",
                            label = "Last Updated",
                            value = formatDate(address.updatedAt)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        AddressDetailRow(
                            icon = "📤",
                            label = "Published",
                            value = formatDate(address.publishedAt)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Quick Actions Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Quick Actions",
                            fontSize = FontSize.MEDIUM,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { /* TODO: Copy address */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("📋 Copy Address")
                            }
                            
                            OutlinedButton(
                                onClick = { /* TODO: Open in maps */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("🗺️ View on Map")
                            }
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
                        onClick = navigateBack,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("← Back to Profile")
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

@Composable
private fun AddressDetailRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = icon,
            fontSize = FontSize.REGULAR,
            modifier = Modifier.padding(end = 12.dp, top = 2.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = FontSize.SMALL,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = FontSize.REGULAR,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        // Extract date part and format it nicely
        val datePart = dateString.take(10)
        val timePart = dateString.substring(11, 19)
        "$datePart at $timePart"
    } catch (e: Exception) {
        dateString
    }
}