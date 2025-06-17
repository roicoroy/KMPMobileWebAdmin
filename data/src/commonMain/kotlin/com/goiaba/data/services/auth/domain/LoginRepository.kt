package com.goiaba.data.services.auth.domain

import com.goiaba.data.models.auth.login.LoginRequest
import com.goiaba.data.models.auth.login.LoginResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): Flow<RequestState<LoginResponse>>
}