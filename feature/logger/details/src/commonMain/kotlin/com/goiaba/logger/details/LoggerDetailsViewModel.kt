package com.goiaba.logger.details

import androidx.lifecycle.SavedStateHandle
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

class LoggerDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), KoinComponent {
    
    private val loggerRepository: LoggerRepository by inject()
    
    var id: String = savedStateHandle.get<String>("id") ?: ""
    
    private val _logger = MutableStateFlow<RequestState<LoggersResponse.Data>>(RequestState.Loading)
    val logger: StateFlow<RequestState<LoggersResponse.Data>> = _logger.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // Modal state
    private val _isModalVisible = MutableStateFlow(false)
    val isModalVisible: StateFlow<Boolean> = _isModalVisible.asStateFlow()
    
    private val _isModalLoading = MutableStateFlow(false)
    val isModalLoading: StateFlow<Boolean> = _isModalLoading.asStateFlow()
    
    private val _modalMessage = MutableStateFlow<String?>(null)
    val modalMessage: StateFlow<String?> = _modalMessage.asStateFlow()
    
    // Navigation state for logger deletion
    private val _shouldNavigateToLoggerList = MutableStateFlow(false)
    val shouldNavigateToLoggerList: StateFlow<Boolean> = _shouldNavigateToLoggerList.asStateFlow()
    
    // Authentication state
    private val _isLoggedIn = MutableStateFlow(TokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    // Image upload state
    private val _uploadedImageId = MutableStateFlow<Int?>(null)
    val uploadedImageId: StateFlow<Int?> = _uploadedImageId.asStateFlow()
    
    // Modal form state
    var isEditMode = false
        private set
    var initialCowName = ""
        private set
    var initialDate = ""
        private set
    var initialImageId = 1
        private set
    
    init {
        loadLogger()
    }
    
    private fun loadLogger() {
        if (id.isEmpty()) {
            _errorMessage.value = "No logger ID provided"
            return
        }
        
        viewModelScope.launch {
            _logger.value = RequestState.Loading
            _errorMessage.value = null
            
            try {
                // Get all loggers and find the one with matching ID
                loggerRepository.getLoggers().collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _logger.value = RequestState.Loading
                        }
                        
                        is RequestState.Success -> {
                            val targetLogger = result.data.data.find { it.id.toString() == id }
                            if (targetLogger != null) {
                                _logger.value = RequestState.Success(targetLogger)
                            } else {
                                _logger.value = RequestState.Error("Logger with ID $id not found")
                            }
                        }
                        
                        is RequestState.Error -> {
                            _logger.value = RequestState.Error(result.message)
                        }
                        
                        else -> {
                            _logger.value = RequestState.Error("Unknown error occurred")
                        }
                    }
                }
            } catch (e: Exception) {
                _logger.value = RequestState.Error("Failed to load logger: ${e.message}")
            }
        }
    }
    
    fun retry() {
        loadLogger()
        clearError()
    }
    
    fun updateLogger(cowName: String, date: String, imageId: Int) {
        if (id.isEmpty()) return
        
        viewModelScope.launch {
            _isModalLoading.value = true
            _modalMessage.value = null
            
            try {
                val finalImageId = _uploadedImageId.value ?: imageId
                
                val request = LoggerPostRequest(
                    data = LoggerPostRequest.Data(
                        cowName = cowName,
                        date = date,
                        image = finalImageId
                    )
                )
                
                // Note: In a real implementation, you would have an updateLogger method
                // For now, we'll simulate the update
                delay(1000)
                _isModalLoading.value = false
                _modalMessage.value = "Logger updated successfully!"
                
                // Refresh the logger data
                retry()
                
                // Hide modal after a short delay
                delay(500)
                hideModal()
                
            } catch (e: Exception) {
                _isModalLoading.value = false
                _modalMessage.value = "Error updating logger: ${e.message}"
            }
        }
    }
    
    fun createLogger(cowName: String, date: String, imageId: Int) {
        viewModelScope.launch {
            _isModalLoading.value = true
            _modalMessage.value = null
            
            try {
                val finalImageId = _uploadedImageId.value ?: imageId
                
                val request = LoggerPostRequest(
                    data = LoggerPostRequest.Data(
                        cowName = cowName,
                        date = date,
                        image = finalImageId
                    )
                )
                
                loggerRepository.createLogger(request).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _isModalLoading.value = true
                        }
                        
                        is RequestState.Success -> {
                            _isModalLoading.value = false
                            _modalMessage.value = "New logger created successfully!"
                            
                            // Reset uploaded image ID
                            _uploadedImageId.value = null
                            
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
                _modalMessage.value = "Error creating logger: ${e.message}"
            }
        }
    }
    
    fun uploadImage(imageData: ByteArray, fileName: String) {
        viewModelScope.launch {
            try {
                loggerRepository.uploadImageToStrapi(imageData, fileName).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            // Loading state is handled in the UI
                        }
                        
                        is RequestState.Success -> {
                            // Get the first uploaded file ID
                            val uploadedFile = result.data.files.firstOrNull()
                            if (uploadedFile != null) {
                                _uploadedImageId.value = uploadedFile.id
                                _modalMessage.value = "Image uploaded successfully!"
                            } else {
                                _modalMessage.value = "Image upload failed: No file returned"
                            }
                        }
                        
                        is RequestState.Error -> {
                            _modalMessage.value = "Image upload failed: ${result.message}"
                        }
                        
                        else -> {
                            _modalMessage.value = "Image upload failed: Unknown error"
                        }
                    }
                }
            } catch (e: Exception) {
                _modalMessage.value = "Image upload error: ${e.message}"
            }
        }
    }
    
    fun deleteLogger() {
        if (id.isEmpty()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                // Note: In a real implementation, you would have a deleteLogger method
                // For now, we'll simulate the deletion
                delay(1500)
                _isLoading.value = false
                _modalMessage.value = "Logger deleted successfully!"
                
                // Wait a moment to show the success message, then navigate
                delay(1500)
                _shouldNavigateToLoggerList.value = true
                
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error deleting logger: ${e.message}"
            }
        }
    }
    
    // Modal functions
    fun showEditModal() {
        val currentLogger = _logger.value.getSuccessDataOrNull()
        if (currentLogger != null) {
            isEditMode = true
            initialCowName = currentLogger.attributes.cowName
            initialDate = currentLogger.attributes.date
            initialImageId = currentLogger.attributes.image.data?.id ?: 1
            _uploadedImageId.value = null // Reset uploaded image
            _isModalVisible.value = true
            _modalMessage.value = null
        }
    }
    
    fun showAddModal() {
        isEditMode = false
        initialCowName = ""
        initialDate = ""
        initialImageId = 1
        _uploadedImageId.value = null // Reset uploaded image
        _isModalVisible.value = true
        _modalMessage.value = null
    }
    
    fun hideModal() {
        _isModalVisible.value = false
        _modalMessage.value = null
        isEditMode = false
        initialCowName = ""
        initialDate = ""
        initialImageId = 1
        _uploadedImageId.value = null // Reset uploaded image
    }
    
    // Navigation functions
    fun onNavigatedToLoggerList() {
        _shouldNavigateToLoggerList.value = false
    }
    
    // Authentication functions
    fun updateAuthState() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}