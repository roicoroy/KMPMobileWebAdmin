package com.goiaba.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.auth.login.LoginRequest
import com.goiaba.data.models.auth.login.LoginResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.services.auth.domain.LoginRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    
    private val loginRepository: LoginRepository by inject()
    
    // Form state
    private val _email = MutableStateFlow("roicoroy@yahoo.com.br")
    val email: StateFlow<String> = _email.asStateFlow()
    
    private val _password = MutableStateFlow("Rwbento123!")
    val password: StateFlow<String> = _password.asStateFlow()
    
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()
    
    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()
    
    // Login state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _loginResult = MutableStateFlow<RequestState<LoginResponse>?>(null)
    val loginResult: StateFlow<RequestState<LoginResponse>?> = _loginResult.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _shouldNavigateToHome = MutableStateFlow(false)
    val shouldNavigateToHome: StateFlow<Boolean> = _shouldNavigateToHome.asStateFlow()
    
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }
    
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null
    }
    
    fun login() {
        if (!validateForm()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            val loginRequest = LoginRequest(
                identifier = _email.value.trim(),
                password = _password.value
            )
            
            try {
                loginRepository.login(loginRequest).collect { result ->
                    _loginResult.value = result
                    
                    when (result) {
                        is RequestState.Loading -> {
                            _isLoading.value = true
                        }
                        
                        is RequestState.Success -> {
                            _isLoading.value = false
                            // Store JWT token and user data
                            storeToken(result.data)
                            _shouldNavigateToHome.value = true
                        }
                        
                        is RequestState.Error -> {
                            _isLoading.value = false
                            _errorMessage.value = result.message
                        }
                        
                        else -> {
                            _isLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Login failed: ${e.message}"
            }
        }
    }
    
    /**
     * Stores the JWT token and user information in local storage
     * and updates the HTTP client to use the new token
     */
    private fun storeToken(loginResponse: LoginResponse) {
        // Store token and user data in local storage
        TokenManager.storeToken(
            jwt = loginResponse.jwt,
            userId = loginResponse.user.id,
            email = loginResponse.user.email,
            username = loginResponse.user.username
        )
        
        // Update the HTTP client to use the new token
        ApiClient.updateAuthToken(loginResponse.jwt)
    }
    
    private fun validateForm(): Boolean {
        var isValid = true
        
        // Validate email
        when {
            _email.value.isBlank() -> {
                _emailError.value = "Email is required"
                isValid = false
            }
            !isValidEmail(_email.value) -> {
                _emailError.value = "Please enter a valid email address"
                isValid = false
            }
            else -> {
                _emailError.value = null
            }
        }
        
        // Validate password
        when {
            _password.value.isBlank() -> {
                _passwordError.value = "Password is required"
                isValid = false
            }
            _password.value.length < 6 -> {
                _passwordError.value = "Password must be at least 6 characters"
                isValid = false
            }
            else -> {
                _passwordError.value = null
            }
        }
        
        return isValid
    }
    
    private fun isValidEmail(email: String): Boolean {
        return true
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun onNavigatedToHome() {
        _shouldNavigateToHome.value = false
    }
    
    /**
     * Logout function to clear stored token and user data
     */
    fun logout() {
        TokenManager.clearToken()
        ApiClient.clearAuthToken()
    }
}