package com.goiaba.data.services.auth.domain

import com.goiaba.data.models.auth.register.RegisterRequest
import com.goiaba.data.models.auth.register.RegisterResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest): Flow<RequestState<RegisterResponse>>
}