package com.goiaba.shared.util

import com.goiaba.shared.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IntentHandler {
    private val _navigateTo = MutableStateFlow<Screen?>(null)
    val navigateTo: StateFlow<Screen?> = _navigateTo.asStateFlow()

    fun resetNavigation() {
        _navigateTo.value = null
    }
}

class IntentHandlerHelper : KoinComponent {
    private val intentHandler: IntentHandler by inject()

    fun navigateToPaymentCompleted(
        isSuccess: Boolean?,
        error: String?,
        token: String?,
    ) {

    }
}