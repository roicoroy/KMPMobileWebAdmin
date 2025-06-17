package com.goiaba.data.services.auth

import com.goiaba.data.models.auth.login.LoginRequest
import com.goiaba.data.models.auth.login.LoginResponse
import com.goiaba.data.services.auth.domain.LoginRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginImpl : LoginRepository {
    
    private val authService = AuthService()
    
    override suspend fun login(loginRequest: LoginRequest): Flow<RequestState<LoginResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            // Add a small delay to show loading state
            delay(500)
            
            val result = authService.login(loginRequest)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to login: ${e.message}"))
        }
    }
}