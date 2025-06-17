package com.goiaba.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.auth.register.RegisterRequest
import com.goiaba.data.models.auth.register.RegisterResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.services.auth.domain.RegisterRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterViewModel : ViewModel(), KoinComponent {
    
    private val registerRepository: RegisterRepository by inject()
    
    // Form state
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()
    
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()
    
    // Error states
    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError: StateFlow<String?> = _usernameError.asStateFlow()
    
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()
    
    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()
    
    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()
    
    // Register state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _registerResult = MutableStateFlow<RequestState<RegisterResponse>?>(null)
    val registerResult: StateFlow<RequestState<RegisterResponse>?> = _registerResult.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _shouldNavigateToHome = MutableStateFlow(false)
    val shouldNavigateToHome: StateFlow<Boolean> = _shouldNavigateToHome.asStateFlow()
    
    fun updateUsername(newUsername: String) {
        _username.value = newUsername
        _usernameError.value = null
    }
    
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }
    
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null
        // Also validate confirm password if it's already filled
        if (_confirmPassword.value.isNotEmpty()) {
            validateConfirmPassword()
        }
    }
    
    fun updateConfirmPassword(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _confirmPasswordError.value = null
        validateConfirmPassword()
    }
    
    private fun validateConfirmPassword() {
        if (_confirmPassword.value.isNotEmpty() && _password.value != _confirmPassword.value) {
            _confirmPasswordError.value = "Passwords do not match"
        } else {
            _confirmPasswordError.value = null
        }
    }
    
    fun register() {
        if (!validateForm()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            val registerRequest = RegisterRequest(
                username = _username.value.trim(),
                email = _email.value.trim(),
                password = _password.value
            )
            
            try {
                registerRepository.register(registerRequest).collect { result ->
                    _registerResult.value = result
                    
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
                _errorMessage.value = "Registration failed: ${e.message}"
            }
        }
    }
    
    /**
     * Stores the JWT token and user information in local storage
     * and updates the HTTP client to use the new token
     */
    private fun storeToken(registerResponse: RegisterResponse) {
        // Store token and user data in local storage
        TokenManager.storeToken(
            jwt = registerResponse.jwt,
            userId = registerResponse.user.id,
            email = registerResponse.user.email,
            username = registerResponse.user.username
        )
        
        // Update the HTTP client to use the new token
        ApiClient.updateAuthToken(registerResponse.jwt)
    }
    
    private fun validateForm(): Boolean {
        var isValid = true
        
        // Validate username
        when {
            _username.value.isBlank() -> {
                _usernameError.value = "Username is required"
                isValid = false
            }
            _username.value.length < 3 -> {
                _usernameError.value = "Username must be at least 3 characters"
                isValid = false
            }
            _username.value.length > 20 -> {
                _usernameError.value = "Username must be less than 20 characters"
                isValid = false
            }
            !_username.value.matches(Regex("^[a-zA-Z0-9_]+$")) -> {
                _usernameError.value = "Username can only contain letters, numbers, and underscores"
                isValid = false
            }
            else -> {
                _usernameError.value = null
            }
        }
        
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
            _password.value.length > 50 -> {
                _passwordError.value = "Password must be less than 50 characters"
                isValid = false
            }
            !hasValidPasswordStrength(_password.value) -> {
                _passwordError.value = "Password must contain at least one letter and one number"
                isValid = false
            }
            else -> {
                _passwordError.value = null
            }
        }
        
        // Validate confirm password
        when {
            _confirmPassword.value.isBlank() -> {
                _confirmPasswordError.value = "Please confirm your password"
                isValid = false
            }
            _password.value != _confirmPassword.value -> {
                _confirmPasswordError.value = "Passwords do not match"
                isValid = false
            }
            else -> {
                _confirmPasswordError.value = null
            }
        }
        
        return isValid
    }
    
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
    
    private fun hasValidPasswordStrength(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun onNavigatedToHome() {
        _shouldNavigateToHome.value = false
    }
}