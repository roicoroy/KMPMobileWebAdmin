package com.goiaba.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
import com.goiaba.profile.ProfileViewModel
import com.goiaba.shared.FontSize
import com.goiaba.shared.Resources
import com.goiaba.shared.util.ImagePickerResult
import com.goiaba.shared.util.rememberImagePicker
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StrapiProfileCard(
    profile: StrapiProfile,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<ProfileViewModel>()

    var shouldPickImage by remember { mutableStateOf(false) }
    val imagePicker = rememberImagePicker()

    LaunchedEffect(shouldPickImage) {
        if (shouldPickImage) {
            try {
                val result = imagePicker.pickImage()
                when (result) {
                    is ImagePickerResult.Success -> {
                        val saved = imagePicker.saveImageToGallery(
                            result.imageData,
                            result.fileName
                        )
                        if (saved) {
                            viewModel.uploadProfileImage(result.imageData, result.fileName)
                        }
                    }
                    is ImagePickerResult.Error -> {}
                    null -> {}
                }
            } catch (e: Exception) {
                // Handle exception
            } finally {
                shouldPickImage = false
            }
        }
    }


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
                                    .clickable { shouldPickImage = true },
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