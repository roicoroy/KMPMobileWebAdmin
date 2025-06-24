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
import com.goiaba.profile.components.AddressCard
import com.goiaba.profile.modals.AddressDetailsModal
import com.goiaba.profile.modals.AddressEditModal
import com.goiaba.shared.*
import com.goiaba.shared.components.InfoCard
import com.goiaba.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAddressListScreen(
    navigateToHome: () -> Unit,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val strapiProfile by viewModel.strapiProfile.collectAsState()
    val isUpdatingAddress by viewModel.isUpdatingAddress.collectAsState()
    val updateMessage by viewModel.updateMessage.collectAsState()

    // Modal state
    var selectedAddress by remember { mutableStateOf<com.goiaba.data.models.profile.strapiUser.StrapiProfile.Data.Addresse?>(null) }
    var showAddAddressModal by remember { mutableStateOf(false) }

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
                                text = "My Addresses",
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
                                onClick = { showAddAddressModal = true }
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Plus),
                                    contentDescription = "Add address",
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
                        subtitle = "Please login to view your addresses"
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
                                    text = "Loading addresses...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    onSuccess = { profileData ->
                        if (profileData.data.addresses.isEmpty()) {
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
                                    image = Resources.Icon.MapPin,
                                    title = "No Addresses Yet",
                                    subtitle = "You haven't added any addresses yet. Tap the + button to add your first address."
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Button(
                                    onClick = { showAddAddressModal = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("🏠 Add First Address")
                                }
                            }
                        } else {
                            // Addresses list
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
                                                text = "🏠",
                                                fontSize = FontSize.LARGE,
                                                modifier = Modifier.padding(end = 12.dp)
                                            )
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "My Addresses",
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                                Text(
                                                    text = "${profileData.data.addresses.size} address${if (profileData.data.addresses.size != 1) "es" else ""}",
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    fontSize = FontSize.SMALL
                                                )
                                            }
                                            TextButton(
                                                onClick = { showAddAddressModal = true }
                                            ) {
                                                Text("+ Add New")
                                            }
                                        }
                                    }
                                }

                                items(profileData.data.addresses) { address ->
                                    AddressCard(
                                        address = address,
                                        onAddressClick = { selectedAddress = it }
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
                                title = "Failed to Load Addresses",
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

        // Address Details Modal
        AddressDetailsModal(
            isVisible = selectedAddress != null,
            address = selectedAddress,
            isLoading = isUpdatingAddress,
            onDismiss = { selectedAddress = null },
            onUpdateAddress = { firstName, lastName, firstLineAddress, secondLineAddress, postCode, city, country, phoneNumber ->
                selectedAddress?.let { address ->
                    viewModel.updateAddress(
                        addressId = address.documentId,
                        firstName = firstName,
                        lastName = lastName,
                        firstLineAddress = firstLineAddress,
                        secondLineAddress = secondLineAddress,
                        postCode = postCode,
                        city = city,
                        country = country,
                        phoneNumber = phoneNumber
                    )
                }
                selectedAddress = null
            },
            onDeleteAddress = {
                selectedAddress?.let { address ->
                    viewModel.deleteAddress(address.documentId.toString() )
                }
                selectedAddress = null
            }
        )

        // Add Address Modal
        AddressEditModal(
            isVisible = showAddAddressModal,
            address = null,
            isLoading = isUpdatingAddress,
            onDismiss = { showAddAddressModal = false },
            onDelete = {
                selectedAddress?.let { address ->
                    viewModel.deleteAddress(address.documentId.toString())
                }
                selectedAddress = null
            },
            onSave = { firstName, lastName, firstLineAddress, secondLineAddress, postCode, city, country, phoneNumber ->
                viewModel.createAddress(
                    firstName = firstName,
                    lastName = lastName,
                    firstLineAddress = firstLineAddress,
                    secondLineAddress = secondLineAddress,
                    postCode = postCode,
                    city = city,
                    country = country,
                    phoneNumber = phoneNumber
                )
                showAddAddressModal = false
            }
        )
    }
}