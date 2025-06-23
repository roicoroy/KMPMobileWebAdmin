package com.goiaba.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
import com.goiaba.shared.FontSize
import com.goiaba.shared.Resources
import org.jetbrains.compose.resources.painterResource

@Composable
fun StrapiProfileCard(
    profile: StrapiProfile,
    modifier: Modifier = Modifier,
    onImageClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with profile info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (profile.data.avatar.url.isNotEmpty()) {
                            AsyncImage(
                                model = profile.data.avatar.url,
                                contentDescription = "Profile avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(50.dp))
                                    .then(
                                        if (onImageClick != null) {
                                            Modifier
                                        } else Modifier
                                    ),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(Resources.Image.Cat),
                                error = painterResource(Resources.Image.Cat)
                            )
                        } else {
                            Icon(
                                painter = painterResource(Resources.Icon.Person),
                                contentDescription = "Default avatar",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.data.user.username,
                        fontSize = FontSize.LARGE,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = profile.data.user.email,
                        fontSize = FontSize.REGULAR,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (profile.data.dob.isNotEmpty()) {
                        Text(
                            text = "Born: ${profile.data.dob}",
                            fontSize = FontSize.SMALL,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Statistics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticCard(
                    icon = "🏠",
                    count = profile.data.addresses.size,
                    label = "Addresses",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatisticCard(
                    icon = "📢",
                    count = profile.data.adverts.size,
                    label = "Adverts",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile details
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
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Profile Information",
                        fontSize = FontSize.MEDIUM,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    DetailRow(
                        icon = "🆔",
                        label = "Profile ID",
                        value = profile.data.id.toString()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailRow(
                        icon = "📄",
                        label = "Document ID",
                        value = profile.data.documentId
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailRow(
                        icon = "📅",
                        label = "Created",
                        value = profile.data.createdAt.take(10)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailRow(
                        icon = "🔄",
                        label = "Updated",
                        value = profile.data.updatedAt.take(10)
                    )

                    if (profile.data.publishedAt.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow(
                            icon = "📤",
                            label = "Published",
                            value = profile.data.publishedAt.take(10)
                        )
                    }
                }
            }
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