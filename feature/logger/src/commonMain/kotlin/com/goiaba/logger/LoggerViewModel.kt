package com.goiaba.logger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.logger.LoggerPostRequest
import com.goiaba.data.models.logger.LoggersResponse
import com.goiaba.data.services.logger.domain.LoggerRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoggerViewModel : ViewModel(), KoinComponent {
    
    private val loggerRepository: LoggerRepository by inject()
    
    private val _loggers = MutableStateFlow<RequestState<LoggersResponse>>(RequestState.Loading)
    val loggers: StateFlow<RequestState<LoggersResponse>> = _loggers.asStateFlow()
    
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
    
    fun loadLoggers() {
        viewModelScope.launch {
            loggerRepository.getLoggers().collect { result ->
                _loggers.value = result
            }
        }
    }
    
    fun refreshLoggers() {
        loadLoggers()
    }
    
    fun createLogger(cowName: String, date: String, imageId: Int) {
        viewModelScope.launch {
            _isModalLoading.value = true
            _modalMessage.value = null
            
            try {
                val request = LoggerPostRequest(
                    data = LoggerPostRequest.Data(
                        cowName = cowName,
                        date = date,
                        image = imageId
                    )
                )
                
                loggerRepository.createLogger(request).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _isModalLoading.value = true
                        }
                        
                        is RequestState.Success -> {
                            _isModalLoading.value = false
                            _modalMessage.value = "Logger created successfully!"
                            // Refresh the loggers list
                            refreshLoggers()
                            // Hide modal after a short delay
                            delay(500)
                            hideModal()
                        }
                        
                        is RequestState.Error -> {
                            _isModalLoading.value = false
                            _modalMessage.value = "Failed to create logger: ${result.message}"
                        }
                        
                        else -> {
                            _isModalLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isModalLoading.value = false
                _modalMessage.value = "Error: ${e.message}"
            }
        }
    }
    
    // Modal functions
    fun showModal() {
        _isModalVisible.value = true
        _modalMessage.value = null
    }
    
    fun hideModal() {
        _isModalVisible.value = false
        _modalMessage.value = null
    }
    
    // Authentication functions
    fun updateAuthState() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
        _userEmail.value = TokenManager.getUserEmail()
    }
}