package com.goiaba.data.services.auth

import com.goiaba.data.models.auth.register.RegisterRequest
import com.goiaba.data.models.auth.register.RegisterResponse
import com.goiaba.data.services.auth.domain.RegisterRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterImpl : RegisterRepository {
    
    private val authService = AuthService()
    
    override suspend fun register(registerRequest: RegisterRequest): Flow<RequestState<RegisterResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            // Add a small delay to show loading state
            delay(500)
            
            val result = authService.register(registerRequest)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to register: ${e.message}"))
        }
    }
}