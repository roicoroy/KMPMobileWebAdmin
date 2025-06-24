package com.goiaba.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun ProfileInfoCard(
    user: StrapiProfile,
    userRole:String?
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    var selectedImageData by remember { mutableStateOf<ByteArray?>(null) }
    var selectedImageName by remember { mutableStateOf<String?>(null) }
    var isUploadingImage by remember { mutableStateOf(false) }
    var shouldPickImage by remember { mutableStateOf(false) }
    val imagePicker = rememberImagePicker()

    LaunchedEffect(shouldPickImage) {
        if (shouldPickImage) {
            isUploadingImage = true
            try {
                val result = imagePicker.pickImage()
                when (result) {
                    is ImagePickerResult.Success -> {
                        // Save to gallery first
                        val saved = imagePicker.saveImageToGallery(
                            result.imageData,
                            result.fileName
                        )

                        if (saved) {
                            // Upload to Strapi
                            viewModel.uploadProfileImage(result.imageData, result.fileName)
                        }
                    }
                    is ImagePickerResult.Error -> {
                        // Handle error - could show a snackbar or error message
                    }
                    null -> {
                        // User cancelled
                    }
                }
            } catch (e: Exception) {
                // Handle exception
            } finally {
                isUploadingImage = false
                shouldPickImage = false
            }
        }
    }

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
                        AsyncImage(
                            model = user.data.avatar.url,
                            contentDescription = "Advert cover",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable { shouldPickImage = true }
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(Resources.Image.Cat),
                            error = painterResource(Resources.Image.Cat)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.data.user.username,
                        fontSize = FontSize.LARGE,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = user.data.user.email,
                        fontSize = FontSize.REGULAR,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if(userRole != null){
                        Text(
                            text = "$userRole",
                            fontSize = FontSize.SMALL,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticCard(
                    icon = "🏠",
                    count = user.data.addresses.size,
                    label = "Addresses",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatisticCard(
                    icon = "📢",
                    count = user.data.adverts.size,
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