package com.goiaba.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.adverts.AdvertGetResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.services.adverts.domain.AdvertRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AdvertsViewModel : ViewModel(), KoinComponent {

    private val advertRepository: AdvertRepository by inject()

    // Adverts state
    private val _adverts = MutableStateFlow<RequestState<AdvertGetResponse>>(RequestState.Loading)
    val adverts: StateFlow<RequestState<AdvertGetResponse>> = _adverts.asStateFlow()

    // Modal state
    private val _isModalVisible = MutableStateFlow(false)
    val isModalVisible: StateFlow<Boolean> = _isModalVisible.asStateFlow()

    private val _isModalLoading = MutableStateFlow(false)
    val isModalLoading: StateFlow<Boolean> = _isModalLoading.asStateFlow()

    private val _modalMessage = MutableStateFlow<String?>(null)
    val modalMessage: StateFlow<String?> = _modalMessage.asStateFlow()

    // Authentication state
    private val _isLoggedIn = MutableStateFlow(TokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userEmail = MutableStateFlow(TokenManager.getUserEmail())
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    init {
        loadAdverts()
    }

    fun loadAdverts() {
        viewModelScope.launch {
            advertRepository.getAdverts().collect { result ->
                _adverts.value = result
            }
        }
    }

    fun refreshAdverts() {
        loadAdverts()
    }

    // Authentication functions
    fun logout() {
        TokenManager.clearToken()
        ApiClient.clearAuthToken()
        _isLoggedIn.value = false
        _userEmail.value = null
    }

    fun updateAuthState() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
        _userEmail.value = TokenManager.getUserEmail()
    }
}