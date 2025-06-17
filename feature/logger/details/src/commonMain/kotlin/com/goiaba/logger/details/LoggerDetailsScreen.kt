package com.goiaba.logger.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.goiaba.data.models.logger.LoggersResponse
import com.goiaba.logger.details.components.LoggerDetailsFormModal
import com.goiaba.shared.FontSize
import com.goiaba.shared.IconPrimary
import com.goiaba.shared.Resources
import com.goiaba.shared.Surface
import com.goiaba.shared.TextPrimary
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggerDetailsScreen(
    navigateBack: () -> Unit,
    navigateToLoggerList: () -> Unit
) {
    val viewModel = koinViewModel<LoggerDetailsViewModel>()
    
    val id = viewModel.id
    val logger by viewModel.logger.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    
    // Modal states
    val isModalVisible by viewModel.isModalVisible.collectAsState()
    val isModalLoading by viewModel.isModalLoading.collectAsState()
    val modalMessage by viewModel.modalMessage.collectAsState()
    
    // Navigation state
    val shouldNavigateToLoggerList by viewModel.shouldNavigateToLoggerList.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle navigation to logger list after deletion
    LaunchedEffect(shouldNavigateToLoggerList) {
        if (shouldNavigateToLoggerList) {
            navigateToLoggerList()
            viewModel.onNavigatedToLoggerList()
        }
    }
    
    // Show snackbar for modal messages
    LaunchedEffect(modalMessage) {
        modalMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    
    // Update auth state when screen is displayed
    LaunchedEffect(Unit) {
        viewModel.updateAuthState()
    }
    
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
                            text = "Logger Details",
                            fontSize = FontSize.LARGE,
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Text(
                                text = "← Back",
                                color = IconPrimary,
                                fontSize = FontSize.REGULAR
                            )
                        }
                    },
                    actions = {
                        // Add new logger button (always visible)
                        IconButton(
                            onClick = { viewModel.showAddModal() }
                        ) {
                            Icon(
                                painter = painterResource(Resources.Icon.Plus),
                                contentDescription = "Add new logger",
                                tint = IconPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        navigationIconContentColor = IconPrimary,
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
                // Show error message if any
                errorMessage?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = FontSize.LARGE,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Error",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontSize = FontSize.REGULAR
                                )
                            }
                            TextButton(
                                onClick = { viewModel.retry() }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                
                // Logger content
                logger.DisplayResult<LoggersResponse.Data>(
                    onLoading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Loading logger details...",
                                    fontSize = FontSize.REGULAR,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    onSuccess = { loggerData ->
                        LoggerDetailsContent(
                            logger = loggerData,
                            isLoggedIn = isLoggedIn,
                            isLoading = isLoading,
                            onEdit = { viewModel.showEditModal() },
                            onDelete = { viewModel.deleteLogger() },
                            onRefresh = { viewModel.retry() },
                            navigateBack = navigateBack
                        )
                    },
                    onError = { message ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            InfoCard(
                                modifier = Modifier,
                                image = Resources.Image.Cat,
                                title = "Failed to Load Logger",
                                subtitle = message
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.retry() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("🔄 Try Again")
                                }
                                
                                OutlinedButton(
                                    onClick = navigateBack
                                ) {
                                    Text("← Go Back")
                                }
                            }
                        }
                    }
                )
                
                // Debug Information (only show if ID is available)
                if (id.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Debug Info",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Logger ID: $id",
                                fontSize = FontSize.SMALL,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Loading: $isLoading",
                                fontSize = FontSize.SMALL,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Logged In: $isLoggedIn",
                                fontSize = FontSize.SMALL,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
        
        // Logger Form Modal with Image Upload
        LoggerDetailsFormModal(
            isVisible = isModalVisible,
            isEditMode = viewModel.isEditMode,
            initialCowName = viewModel.initialCowName,
            initialDate = viewModel.initialDate,
            initialImageId = viewModel.initialImageId,
            isLoading = isModalLoading,
            onDismiss = { viewModel.hideModal() },
            onSubmit = { cowName: String, date: String, imageId: Int ->
                if (viewModel.isEditMode) {
                    viewModel.updateLogger(cowName, date, imageId)
                } else {
                    viewModel.createLogger(cowName, date, imageId)
                }
            },
            onImageUpload = { imageData: ByteArray, fileName: String ->
                viewModel.uploadImage(imageData, fileName)
            }
        )
    }
}

@Composable
private fun LoggerDetailsContent(
    logger: LoggersResponse.Data,
    isLoggedIn: Boolean,
    isLoading: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onRefresh: () -> Unit,
    navigateBack: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Logger ID Badge
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "🐄 Logger ID: ${logger.id}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = FontSize.SMALL,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            // Cow Image
            val imageUrl = logger.attributes.image.data?.attributes?.let { imageAttrs ->
                imageAttrs.formats?.large?.url ?: imageAttrs.url
            }
            
            if (imageUrl != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Cow image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(Resources.Image.Cat),
                        error = painterResource(Resources.Image.Cat)
                    )
                }
            }
            
            // Cow Name
            Text(
                text = logger.attributes.cowName,
                fontSize = FontSize.EXTRA_LARGE,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            
            // Metadata Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Logger Information",
                        fontSize = FontSize.MEDIUM,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Date
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📅",
                            fontSize = FontSize.REGULAR,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "Date",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = logger.attributes.date,
                                fontSize = FontSize.REGULAR,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    // Created Date
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🕒",
                            fontSize = FontSize.REGULAR,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "Created",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = logger.attributes.createdAt,
                                fontSize = FontSize.REGULAR,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    // Updated Date
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔄",
                            fontSize = FontSize.REGULAR,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "Last Updated",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = logger.attributes.updatedAt,
                                fontSize = FontSize.REGULAR,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    // Image Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (logger.attributes.image.data != null) "🖼️" else "❌",
                            fontSize = FontSize.REGULAR,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "Image Status",
                                fontSize = FontSize.SMALL,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (logger.attributes.image.data != null) "Available" else "Missing",
                                fontSize = FontSize.REGULAR,
                                color = if (logger.attributes.image.data != null) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.error
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Edit and Delete buttons (only show if logged in)
                if (isLoggedIn) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onEdit,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("✏️ Edit Logger")
                        }
                        
                        Button(
                            onClick = onDelete,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.height(16.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onError
                                )
                            } else {
                                Text("🗑️ Delete")
                            }
                        }
                    }
                }
                
                // Navigation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = navigateBack,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("← Go Back")
                    }
                    
                    OutlinedButton(
                        onClick = onRefresh,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("🔄 Refresh")
                    }
                }
            }
        }
    }
}