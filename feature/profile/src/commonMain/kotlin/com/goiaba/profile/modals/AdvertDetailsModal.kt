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
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
import com.goiaba.profile.components.DetailRow
import com.goiaba.profile.utils.formatDate
import com.goiaba.shared.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdvertDetailsModal(
    isVisible: Boolean,
    advert: StrapiProfile.Data.Advert?,
    onDismiss: () -> Unit
) {
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
                                text = "Advert Details",
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
                        
                        // Content
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Main Advert Card
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
                                                text = "📢 ID: ${advert.id}",
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
                                                text = "✅ Published",
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                fontSize = FontSize.SMALL,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(20.dp))
                                    
                                    // Title
                                    Text(
                                        text = advert.title,
                                        fontSize = FontSize.EXTRA_LARGE,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Description
                                    if (advert.description.isNotBlank()) {
                                        Text(
                                            text = advert.description,
                                            fontSize = FontSize.REGULAR,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = FontSize.EXTRA_REGULAR
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                    
                                    // Slug Section
                                    advert.slug?.let { slug ->
                                        if (slug.isNotBlank()) {
                                            DetailRow(
                                                icon = "🔗",
                                                label = "URL Slug",
                                                value = slug
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                        }
                                    }
                                    
                                    // Document ID
                                    DetailRow(
                                        icon = "📄",
                                        label = "Document ID",
                                        value = advert.documentId
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
                                        text = "Timeline Information",
                                        fontSize = FontSize.MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    
                                    DetailRow(
                                        icon = "📅",
                                        label = "Created",
                                        value = formatDate(advert.createdAt)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    DetailRow(
                                        icon = "🔄",
                                        label = "Last Updated",
                                        value = formatDate(advert.updatedAt)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    DetailRow(
                                        icon = "📤",
                                        label = "Published",
                                        value = formatDate(advert.publishedAt)
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
                                onClick = { /* TODO: Edit functionality */ },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("✏️ Edit Advert")
                            }
                        }
                    }
                }
            }
        }
    }
}