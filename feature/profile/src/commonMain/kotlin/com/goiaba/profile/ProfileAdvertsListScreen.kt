package com.goiaba.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.profile.components.AdvertCard
import com.goiaba.profile.modals.AdvertCreateModal
import com.goiaba.profile.modals.AdvertDetailsModal
import com.goiaba.profile.modals.AdvertEditModal
import com.goiaba.shared.*
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAdvertsListScreen(
    navigateToHome: () -> Unit,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val strapiProfile by viewModel.strapiProfile.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val isUpdatingAdvert by viewModel.isUpdatingAdvert.collectAsState()
    val updateMessage by viewModel.updateMessage.collectAsState()

    // Modal state
    var selectedAdvert by remember { mutableStateOf<com.goiaba.data.models.profile.strapiUser.StrapiProfile.Data.Advert?>(null) }
    var showAddAdvertModal by remember { mutableStateOf(false) }
    var showEditAdvertModal by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.updateAuthState()
        viewModel.loadCategories()
    }

    // Show update messages
    LaunchedEffect(updateMessage) {
        updateMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearUpdateMessage()
        }
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
                        Column {
                            Text(
                                text = "My Adverts",
                                fontSize = FontSize.LARGE,
                                color = TextPrimary
                            )
                            if (isLoggedIn && userEmail != null) {
                                Text(
                                    text = userEmail!!,
                                    fontSize = FontSize.SMALL,
                                    color = TextPrimary.copy(alpha = 0.7f)
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateToHome) {
                            Icon(
                                painter = painterResource(Resources.Icon.BackArrow),
                                contentDescription = "Back",
                                tint = IconPrimary
                            )
                        }
                    },
                    actions = {
                        if (isLoggedIn) {
                            IconButton(
                                onClick = { showAddAdvertModal = true }
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Plus),
                                    contentDescription = "Add advert",
                                    tint = IconPrimary
                                )
                            }
                            IconButton(
                                onClick = { viewModel.refreshProfile() }
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Refresh),
                                    contentDescription = "Refresh",
                                    tint = IconPrimary
                                )
                            }
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
        ) { padding ->
            if (!isLoggedIn) {
                // Not logged in state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InfoCard(
                        modifier = Modifier,
                        image = Resources.Icon.Person,
                        title = "Login Required",
                        subtitle = "Please login to view your adverts"
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = navigateToHome,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("← Go Back")
                    }
                }
            } else {
                // Logged in state
                strapiProfile.DisplayResult(
                    onLoading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Loading adverts...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    onSuccess = { profileData ->
                        if (profileData.data.adverts.isEmpty()) {
                            // Empty state
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                InfoCard(
                                    modifier = Modifier,
                                    image = Resources.Image.Cat,
                                    title = "No Adverts Yet",
                                    subtitle = "You haven't created any adverts yet. Tap the + button to create your first advert."
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Button(
                                    onClick = { showAddAdvertModal = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("📢 Create First Advert")
                                }
                            }
                        } else {
                            // Adverts list
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
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
                                            Text(
                                                text = "📢",
                                                fontSize = FontSize.LARGE,
                                                modifier = Modifier.padding(end = 12.dp)
                                            )
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "My Adverts",
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                                Text(
                                                    text = "${profileData.data.adverts.size} advert${if (profileData.data.adverts.size != 1) "s" else ""}",
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    fontSize = FontSize.SMALL
                                                )
                                            }
                                            TextButton(
                                                onClick = { showAddAdvertModal = true }
                                            ) {
                                                Text("+ Add New")
                                            }
                                        }
                                    }
                                }

                                items(profileData.data.adverts) { advert ->
                                    AdvertCard(
                                        advert = advert,
                                        onAdvertClick = { 
                                            selectedAdvert = it
                                            showEditAdvertModal = true
                                        }
                                    )
                                }
                                
                                // Add some bottom padding
                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    },
                    onError = { message ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            InfoCard(
                                modifier = Modifier,
                                image = Resources.Image.Cat,
                                title = "Failed to Load Adverts",
                                subtitle = message
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.refreshProfile() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("🔄 Retry")
                                }
                                
                                OutlinedButton(
                                    onClick = navigateToHome
                                ) {
                                    Text("← Go Back")
                                }
                            }
                        }
                    }
                )
            }
        }

        // Create Advert Modal
        categories.DisplayResult(
            onSuccess = { categoriesData ->
                AdvertCreateModal(
                    isVisible = showAddAdvertModal,
                    categories = categoriesData.data,
                    isLoading = isUpdatingAdvert,
                    onDismiss = { showAddAdvertModal = false },
                    onSave = { title, description, categoryId, slug ->
                        viewModel.createAdvert(
                            title = title,
                            description = description,
                            categoryId = categoryId,
                            slug = slug
                        )
                        showAddAdvertModal = false
                    }
                )
            },
            onError = { },
            onLoading = { }
        )

        // Edit Advert Modal
        categories.DisplayResult(
            onSuccess = { categoriesData ->
                AdvertEditModal(
                    isVisible = showEditAdvertModal,
                    advert = selectedAdvert,
                    categories = categoriesData.data,
                    isLoading = isUpdatingAdvert,
                    onDismiss = { 
                        showEditAdvertModal = false
                        selectedAdvert = null
                    },
                    onSave = { title, description, categoryId, slug ->
                        selectedAdvert?.let { advert ->
                            viewModel.updateAdvert(
                                advertId = advert.documentId,
                                title = title,
                                description = description,
                                categoryId = categoryId,
                                slug = slug
                            )
                        }
                        showEditAdvertModal = false
                        selectedAdvert = null
                    },
                    onDelete = {
                        selectedAdvert?.let { advert ->
                            viewModel.deleteAdvert(advert.documentId)
                        }
                        showEditAdvertModal = false
                        selectedAdvert = null
                    }
                )
            },
            onError = { },
            onLoading = { }
        )
    }
}