package com.goiaba.feature

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.goiaba.shared.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    navigateToProfile: () -> Unit,
    navigateToAdvertScreen: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToAdvertDetails: (String) -> Unit = {}
) {
    val viewModel = koinViewModel<HomeGraphViewModel>()

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

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
                        Column {
                            Text(
                                text = "Home Screen",
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
                    actions = {
                        if (isLoggedIn) {
                            // Logout button
                            IconButton(
                                onClick = { viewModel.logout() }
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.SignOut),
                                    contentDescription = "Logout",
                                    tint = IconPrimary
                                )
                            }
                            IconButton(
                                onClick = { navigateToAdvertScreen() }
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Book),
                                    contentDescription = "Adverts",
                                    tint = IconPrimary
                                )
                            }
                            IconButton(
                                onClick = { navigateToProfile() }
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Person),
                                    contentDescription = "Adverts",
                                    tint = IconPrimary
                                )
                            }
                        } else {
                            // Login button
                            IconButton(
                                onClick = navigateToLogin
                            ) {
                                Icon(
                                    painter = painterResource(Resources.Icon.SignIn),
                                    contentDescription = "Login",
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
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
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
                                    text = "Login to access all features",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontSize = FontSize.SMALL
                                )
                            }
                            TextButton(
                                onClick = navigateToLogin
                            ) {
                                Text("Login")
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
//                            on the profile, the user has to update the info, date fo birth, avatar, add an address.
//                            if user is a professional, he/she can post an add
                            text = "Check if user has profile, if not create one.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

