package com.goiaba.home.advert.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.adverts.AdvertGetResponse
import com.goiaba.data.services.adverts.domain.AdvertRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AdvertDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), KoinComponent {

    private val advertRepository: AdvertRepository by inject()

    var id: String = savedStateHandle.get<String>("id") ?: ""

    private val _advert = MutableStateFlow<RequestState<AdvertGetResponse.Advert>>(RequestState.Loading)
    val advert: StateFlow<RequestState<AdvertGetResponse.Advert>> = _advert.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Authentication state
    private val _isLoggedIn = MutableStateFlow(TokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        loadAdvert()
    }

    private fun loadAdvert() {
        if (id.isEmpty()) {
            _errorMessage.value = "No advert ID provided"
            return
        }

        viewModelScope.launch {
            _advert.value = RequestState.Loading
            _errorMessage.value = null

            try {
                // Get all adverts and find the one with matching ID
                advertRepository.getAdverts().collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _advert.value = RequestState.Loading
                        }

                        is RequestState.Success -> {
                            val targetAdvert = result.data.data.find { it.id.toString() == id }
                            if (targetAdvert != null) {
                                _advert.value = RequestState.Success(targetAdvert)
                            } else {
                                _advert.value = RequestState.Error("Advert with ID $id not found")
                            }
                        }

                        is RequestState.Error -> {
                            _advert.value = RequestState.Error(result.message)
                        }

                        else -> {
                            _advert.value = RequestState.Error("Unknown error occurred")
                        }
                    }
                }
            } catch (e: Exception) {
                _advert.value = RequestState.Error("Failed to load advert: ${e.message}")
            }
        }
    }

    fun retry() {
        loadAdvert()
        clearError()
    }

    // Authentication functions
    fun updateAuthState() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
    }

    fun clearError() {
        _errorMessage.value = null
    }
}