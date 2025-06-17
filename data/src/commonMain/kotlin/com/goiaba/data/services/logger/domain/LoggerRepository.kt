package com.goiaba.data.services.logger.domain

import com.goiaba.data.models.logger.LoggerPostRequest
import com.goiaba.data.models.logger.LoggerPostResponse
import com.goiaba.data.models.logger.LoggersResponse
import com.goiaba.data.services.logger.UploadResponse
import com.goiaba.data.services.logger.UploadedImage
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface LoggerRepository {
    fun getLoggers(): Flow<RequestState<LoggersResponse>>
    suspend fun createLogger(request: LoggerPostRequest): Flow<RequestState<LoggerPostResponse>>
    suspend fun getUploadedImages(): Flow<RequestState<List<UploadedImage>>>
    suspend fun uploadImageToStrapi(imageData: ByteArray, fileName: String): Flow<RequestState<UploadResponse>>
}