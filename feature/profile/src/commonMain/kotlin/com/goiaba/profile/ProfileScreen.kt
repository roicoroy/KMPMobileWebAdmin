package com.goiaba.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
import com.goiaba.profile.modals.AddressEditModal
import com.goiaba.profile.components.ProfileInfoCard
import com.goiaba.profile.modals.AddressDetailsModal
import com.goiaba.profile.modals.AdvertDetailsModal
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
    val userRole by viewModel.userRole.collectAsState()
    val user by viewModel.user.collectAsState()
    val strapiProfile by viewModel.strapiProfile.collectAsState()
    val isUpdatingAddress by viewModel.isUpdatingAddress.collectAsState()
    val updateMessage by viewModel.updateMessage.collectAsState()

    // State for modals
    var selectedAdvert by remember { mutableStateOf<com.goiaba.data.models.profile.Advert?>(null) }
    var selectedAddress by remember { mutableStateOf<com.goiaba.data.models.profile.Addresse?>(null) }

    // State for add address modal
    var showAddAddressModal by remember { mutableStateOf(false) }
    var initialImageId: Int = 1
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
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                painter = painterResource(Resources.Icon.BackArrow),
                                contentDescription = "Back",
                                tint = White
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
                                    tint = White
                                )
                            }
                            Button(onClick = { navigateToAdvertsListScreen() }) {
                                Text("Adverts")
                            }
                            Button(onClick = { navigateToAddressListScreen() }) {
                                Text("Addresses")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Black,
                        scrolledContainerColor = Surface,
                        titleContentColor = White,
                        actionIconContentColor = White
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                strapiProfile.DisplayResult(
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
                                    text = "Loading profile...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    onSuccess = { userResponse ->
                        ProfileInfoCard(user = userResponse, userRole)
                    },
                    onError = { message ->
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { viewModel.refreshProfile() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("🔄 Retry")
                            }
                            InfoCard(
                                modifier = Modifier,
                                image = Resources.Image.Cat,
                                title = "Failed to Load Profile",
                                subtitle = message
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                )
            }
        }

        // Add Address Modal
        AddressEditModal(
            isVisible = showAddAddressModal,
            address = null, // null for creating new address
            isLoading = isUpdatingAddress,
            onDismiss = { showAddAddressModal = false },
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
            },
            onDeleteAddress = {
                selectedAddress?.let { address ->
                    viewModel.deleteAddress(address.documentId)
                    selectedAddress = null
                }
            }
        )

        // Advert Details Modal
        AdvertDetailsModal(
            isVisible = selectedAdvert != null,
            advert = selectedAdvert,
            onDismiss = { selectedAdvert = null }
        )
    }
}
