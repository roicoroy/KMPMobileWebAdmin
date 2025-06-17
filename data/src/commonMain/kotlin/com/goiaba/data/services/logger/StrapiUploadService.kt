package com.goiaba.data.services.logger

import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.uploadFiles
import com.goiaba.shared.util.RequestState
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class StrapiUploadService {
    
    suspend fun uploadImage(imageData: ByteArray, fileName: String): RequestState<UploadResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.post(uploadFiles) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("files", imageData, Headers.build {
                                append(HttpHeaders.ContentType, "image/jpeg")
                                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                            })
                        }
                    )
                )
            }
            
            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val uploadResponse = response.body<List<UploadedFile>>()
                    RequestState.Success(UploadResponse(uploadResponse))
                }
                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Bad request: Invalid image data")
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Please login to upload images")
                }
                HttpStatusCode.PayloadTooLarge -> {
                    RequestState.Error("Image too large: Please select a smaller image")
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

@Serializable
data class UploadResponse(
    val files: List<UploadedFile>
)

@Serializable
data class UploadedFile(
    val id: Int,
    val name: String,
    val alternativeText: String? = null,
    val caption: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val formats: ImageFormats? = null,
    val hash: String,
    val ext: String,
    val mime: String,
    val size: Double,
    val url: String,
    val previewUrl: String? = null,
    val provider: String,
    val provider_metadata: ProviderMetadata? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class ImageFormats(
    val thumbnail: ImageFormat? = null,
    val small: ImageFormat? = null,
    val medium: ImageFormat? = null,
    val large: ImageFormat? = null
)

@Serializable
data class ImageFormat(
    val name: String,
    val hash: String,
    val ext: String,
    val mime: String,
    val width: Int,
    val height: Int,
    val size: Double,
    val url: String
)

@Serializable
data class ProviderMetadata(
    val public_id: String,
    val resource_type: String
)