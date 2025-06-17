package com.goiaba.data.services.logger

import com.goiaba.data.models.logger.LoggerPostRequest
import com.goiaba.data.models.logger.LoggerPostResponse
import com.goiaba.data.models.logger.LoggersResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.getAllLoggers
import com.goiaba.data.networking.getAllUploadFiles
import com.goiaba.shared.util.RequestState
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class LoggerService {
    
    suspend fun getLoggers(): RequestState<LoggersResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get(getAllLoggers)
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val loggersResponse = response.body<LoggersResponse>()
                    RequestState.Success(loggersResponse)
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
                }
                HttpStatusCode.NotFound -> {
                    RequestState.Error("Loggers endpoint not found")
                }
                HttpStatusCode.InternalServerError -> {
                    RequestState.Error("Server error: Please try again later")
                }
                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }
    
    suspend fun createLogger(request: LoggerPostRequest): RequestState<LoggerPostResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.post(getAllLoggers) {
                setBody(request)
            }
            
            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val loggerResponse = response.body<LoggerPostResponse>()
                    RequestState.Success(loggerResponse)
                }
                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Bad request: Invalid logger data")
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Please login to create loggers")
                }
                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }
    
    // Get available images for the logger
    suspend fun getUploadedImages(): RequestState<List<UploadedImage>> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get(getAllUploadFiles)
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val images = response.body<List<UploadedImage>>()
                    RequestState.Success(images)
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
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

// Simplified uploaded image model for the dropdown
@kotlinx.serialization.Serializable
data class UploadedImage(
    val id: Int,
    val name: String,
    val url: String
)