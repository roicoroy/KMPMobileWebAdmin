package com.goiaba.logger

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.goiaba.data.models.logger.LoggersResponse
import com.goiaba.logger.components.LoggerFormModal
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
fun LoggerScreen(
    navigateToDetails: (String) -> Unit,
    navigateToHome: () -> Unit,
) {
    val viewModel = koinViewModel<LoggerViewModel>()
    
    val loggers by viewModel.loggers.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    
    // Modal states
    val isModalVisible by viewModel.isModalVisible.collectAsState()
    val isModalLoading by viewModel.isModalLoading.collectAsState()
    val modalMessage by viewModel.modalMessage.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Update auth state when screen is displayed
    LaunchedEffect(Unit) {
        viewModel.updateAuthState()
        viewModel.loadLoggers()
    }
    
    // Show snackbar for modal messages
    LaunchedEffect(modalMessage) {
        modalMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Cow Loggers",
                            fontSize = FontSize.LARGE,
                            color = TextPrimary
                        )
                        if (isLoggedIn && userEmail != null) {
                            Text(
                                text = "Welcome, $userEmail",
                                fontSize = FontSize.SMALL,
                                color = TextPrimary.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateToHome) {
                        Text(
                            text = "← Home",
                            color = IconPrimary,
                            fontSize = FontSize.REGULAR
                        )
                    }
                },
                actions = {
                    // Show add logger button only if logged in
                    if (isLoggedIn) {
                        IconButton(
                            onClick = { viewModel.showModal() }
                        ) {
                            Icon(
                                painter = painterResource(Resources.Icon.Plus),
                                contentDescription = "Add new logger",
                                tint = IconPrimary
                            )
                        }
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
        ) {
            // Authentication status card
            if (!isLoggedIn) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.SignIn),
                            contentDescription = "Login info",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Not logged in",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Login to create new cow loggers",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = FontSize.SMALL
                            )
                        }
                        TextButton(
                            onClick = navigateToHome
                        ) {
                            Text("Go to Login")
                        }
                    }
                }
            }
            
            // Loggers list
            loggers.DisplayResult(
                onLoading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading cow loggers...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                onSuccess = { loggersResponse ->
                    if (loggersResponse.data.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            InfoCard(
                                image = Resources.Image.Cat,
                                title = "No Loggers Found",
                                subtitle = "There are no cow loggers available at the moment."
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(loggersResponse.data) { logger ->
                                LoggerCard(
                                    logger = logger,
                                    onClick = { navigateToDetails(logger.id.toString()) }
                                )
                            }
                            
                            // Add some bottom padding for the last item
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                },
                onError = { message ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InfoCard(
                            modifier = Modifier,
                            image = Resources.Image.Cat,
                            title = "Failed to Load Loggers",
                            subtitle = message
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { viewModel.refreshLoggers() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("🔄 Retry")
                        }
                    }
                }
            )
        }
        
        // Logger Form Modal (only show if logged in)
        if (isLoggedIn) {
            LoggerFormModal(
                isVisible = isModalVisible,
                isLoading = isModalLoading,
                onDismiss = { viewModel.hideModal() },
                onSubmit = { cowName, date, imageId ->
                    viewModel.createLogger(cowName, date, imageId)
                }
            )
        }
    }
}

@Composable
private fun LoggerCard(
    logger: LoggersResponse.Data,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cow image - with safe access to image data
            val imageUrl = logger.attributes.image.data?.attributes?.let { imageAttrs ->
                // Try to get thumbnail first, then fallback to main URL
                imageAttrs.formats?.thumbnail?.url ?: imageAttrs.url
            }
            
            AsyncImage(
                model = imageUrl,
                contentDescription = "Cow image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(Resources.Image.Cat),
                error = painterResource(Resources.Image.Cat)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Logger details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = logger.attributes.cowName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Date: ${logger.attributes.date}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "ID: ${logger.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Created: ${logger.attributes.createdAt.take(10)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Show image status
                val imageStatus = if (logger.attributes.image.data != null) {
                    "Image: ✅ Available"
                } else {
                    "Image: ❌ Missing"
                }
                
                Text(
                    text = imageStatus,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (logger.attributes.image.data != null) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
            
            // Visual indicator for clickable item
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "→",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}