package com.goiaba.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.profile.components.StrapiProfileCard
import com.goiaba.profile.components.StrapiUserCard
import com.goiaba.shared.*
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateToAdvertsListScreen: () -> Unit,
    navigateToAddressListScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<ProfileViewModel>()

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val user by viewModel.user.collectAsState()
    val strapiProfile by viewModel.strapiProfile.collectAsState()
    val updateMessage by viewModel.updateMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.updateAuthState()
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
                                text = "Profile",
                                fontSize = FontSize.LARGE,
                                color = TextPrimary
                            )
//                            if (isLoggedIn && userEmail != null) {
//                                Text(
//                                    text = userEmail,
//                                    fontSize = FontSize.SMALL,
//                                    color = TextPrimary.copy(alpha = 0.7f)
//                                )
//                            }
                        }
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
                        if (isLoggedIn) {
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
                        subtitle = "Please login to view your profile"
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = navigateBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("← Go Back")
                    }
                }
            } else {
                // Logged in state
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // User card
                    item {
                        user.DisplayResult(
                            onLoading = {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            },
                            onSuccess = { userData ->
                                StrapiUserCard(user = userData)
                            },
                            onError = { message ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Failed to load user data",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        Text(
                                            text = message,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            fontSize = FontSize.SMALL
                                        )
                                    }
                                }
                            }
                        )
                    }

                    // Profile card
                    item {
                        strapiProfile.DisplayResult(
                            onLoading = {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            },
                            onSuccess = { profileData ->
                                StrapiProfileCard(
                                    profile = profileData,
                                    onImageClick = {
                                        // Handle image click for upload
                                    }
                                )
                            },
                            onError = { message ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Failed to load profile data",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        Text(
                                            text = message,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            fontSize = FontSize.SMALL
                                        )
                                    }
                                }
                            }
                        )
                    }

                    // Navigation buttons
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = navigateToAdvertsListScreen,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "📢",
                                        fontSize = FontSize.LARGE
                                    )
                                    Text("My Adverts")
                                }
                            }

                            Button(
                                onClick = navigateToAddressListScreen,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "🏠",
                                        fontSize = FontSize.LARGE
                                    )
                                    Text("My Addresses")
                                }
                            }
                        }
                    }

                    // Add some bottom padding
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}