package com.goiaba.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.goiaba.shared.FontSize
import com.goiaba.shared.IconPrimary
import com.goiaba.shared.Resources
import com.goiaba.shared.Surface
import com.goiaba.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAddressListScreen(
    navigateToHome: () -> Unit,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val text by viewModel.welcomeText.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profile Address List Screen",
                            fontSize = FontSize.LARGE,
                            color = TextPrimary
                        )
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
                Text(text)
            }
        }
    }
}