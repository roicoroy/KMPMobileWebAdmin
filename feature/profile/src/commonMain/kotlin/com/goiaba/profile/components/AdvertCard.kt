package com.goiaba.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.data.models.profile.Advert
import com.goiaba.shared.FontSize
import com.goiaba.shared.Resources
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdvertCard(
    advert: Advert,
    onAdvertClick: ((Advert) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onAdvertClick != null) {
                    Modifier.clickable { onAdvertClick(advert) }
                } else Modifier
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with advert icon and title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "📢",
                            fontSize = FontSize.MEDIUM
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = advert.title,
                        fontSize = FontSize.MEDIUM,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "ID: ${advert.id}",
                        fontSize = FontSize.SMALL,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Status badge and clickable indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "✅ Active",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = FontSize.SMALL,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    
                    if (onAdvertClick != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "→",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Description
            advert.description?.let { description ->
                if (description.isNotBlank()) {
                    Text(
                        text = description,
                        fontSize = FontSize.REGULAR,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
            
            // Advert details
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Document ID
                AdvertDetailRow(
                    icon = "📄",
                    label = "Document ID",
                    value = advert.documentId
                )
                
                // Slug
                advert.slug?.let { slug ->
                    if (slug.isNotBlank()) {
                        AdvertDetailRow(
                            icon = "🔗",
                            label = "Slug",
                            value = slug
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Metadata
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Created",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = advert.createdAt.take(10),
                                fontSize = FontSize.SMALL,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column {
                            Text(
                                text = "Updated",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = advert.updatedAt.take(10),
                                fontSize = FontSize.SMALL,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column {
                            Text(
                                text = "Published",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = advert.publishedAt.take(10),
                                fontSize = FontSize.SMALL,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdvertDetailRow(
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
            modifier = Modifier.padding(end = 8.dp, top = 2.dp)
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