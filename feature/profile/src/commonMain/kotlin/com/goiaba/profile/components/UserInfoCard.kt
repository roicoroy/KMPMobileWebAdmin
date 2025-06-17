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
import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.shared.FontSize
import com.goiaba.shared.Resources
import org.jetbrains.compose.resources.painterResource

@Composable
fun UserInfoCard(
    user: UsersMeResponse
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with user icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.Person),
                            contentDescription = "User",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.username,
                        fontSize = FontSize.LARGE,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = user.email,
                        fontSize = FontSize.REGULAR,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Professional badge
//                if (user.professional) {
//                    Surface(
//                        color = MaterialTheme.colorScheme.secondaryContainer,
//                        shape = RoundedCornerShape(20.dp)
//                    ) {
//                        Text(
//                            text = "💼 Pro",
//                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                            fontSize = FontSize.SMALL,
//                            fontWeight = FontWeight.Medium,
//                            color = MaterialTheme.colorScheme.onSecondaryContainer
//                        )
//                    }
//                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // User details section
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
                    
                    // User ID
                    UserDetailRow(
                        icon = "🆔",
                        label = "User ID",
                        value = user.id.toString()
                    )
                    
                    // Document ID
                    UserDetailRow(
                        icon = "📄",
                        label = "Document ID",
                        value = user.documentId
                    )
                    
                    // Provider
                    UserDetailRow(
                        icon = "🔐",
                        label = "Provider",
                        value = user.provider.replaceFirstChar { it.uppercase() }
                    )
                    
                    // Account Status
                    UserDetailRow(
                        icon = if (user.confirmed) "✅" else "❌",
                        label = "Status",
                        value = if (user.confirmed) "Confirmed" else "Unconfirmed"
                    )
                    
//                    // Account Type
//                    UserDetailRow(
//                        icon = if (user.professional) "💼" else "👤",
//                        label = "Account Type",
//                        value = if (user.professional) "Professional" else "Regular User"
//                    )
                    
                    // Blocked Status
                    if (user.blocked) {
                        UserDetailRow(
                            icon = "🚫",
                            label = "Account Status",
                            value = "Blocked"
                        )
                    }
                    
                    // Created Date
                    UserDetailRow(
                        icon = "📅",
                        label = "Member Since",
                        value = user.createdAt.take(10)
                    )
                    
                    // Last Updated
                    UserDetailRow(
                        icon = "🔄",
                        label = "Last Updated",
                        value = user.updatedAt.take(10),
                        isLast = true
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Statistics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticCard(
                    icon = "🏠",
                    count = user.addresses.size,
                    label = "Addresses",
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                StatisticCard(
                    icon = "📢",
                    count = user.adverts.size,
                    label = "Adverts",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun UserDetailRow(
    icon: String,
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (isLast) 0.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = FontSize.REGULAR,
            modifier = Modifier.padding(end = 8.dp)
        )
        Column {
            Text(
                text = label,
                fontSize = FontSize.SMALL,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = FontSize.REGULAR,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatisticCard(
    icon: String,
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
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
                text = icon,
                fontSize = FontSize.LARGE
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count.toString(),
                fontSize = FontSize.EXTRA_MEDIUM,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = label,
                fontSize = FontSize.SMALL,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}