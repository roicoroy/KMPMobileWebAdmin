package com.goiaba

import androidx.compose.ui.window.ComposeUIViewController
import com.goiaba.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }