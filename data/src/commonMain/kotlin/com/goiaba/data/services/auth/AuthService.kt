package com.goiaba.data.services.auth

import com.goiaba.data.models.auth.login.LoginRequest
import com.goiaba.data.models.auth.login.LoginResponse
import com.goiaba.data.models.auth.register.RegisterRequest
import com.goiaba.data.models.auth.register.RegisterResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.loginRequest
import com.goiaba.data.networking.registerRequest
import com.goiaba.shared.util.RequestState
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class AuthService {
    
    suspend fun login(request: LoginRequest): RequestState<LoginResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.post(loginRequest) {
                setBody(request)
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val loginResponse = response.body<LoginResponse>()
                    RequestState.Success(loginResponse)
                }
                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Invalid credentials. Please check your email and password.")
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Invalid email or password.")
                }
                HttpStatusCode.NotFound -> {
                    RequestState.Error("Login endpoint not found.")
                }
                HttpStatusCode.InternalServerError -> {
                    RequestState.Error("Server error: Please try again later.")
                }
                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }
    
    suspend fun register(request: RegisterRequest): RequestState<RegisterResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.post(registerRequest) {
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val registerResponse = response.body<RegisterResponse>()
                    RequestState.Success(registerResponse)
                }
                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Invalid registration data. Please check your information.")
                }
                HttpStatusCode.Conflict -> {
                    RequestState.Error("Email or username already exists. Please use different credentials.")
                }
                HttpStatusCode.UnprocessableEntity -> {
                    RequestState.Error("Registration failed. Please check your information and try again.")
                }
                HttpStatusCode.InternalServerError -> {
                    RequestState.Error("Server error: Please try again later.")
                }
                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }
}