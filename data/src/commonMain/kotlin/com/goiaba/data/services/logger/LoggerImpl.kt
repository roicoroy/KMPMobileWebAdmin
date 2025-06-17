package com.goiaba.data.services.logger

import com.goiaba.data.models.logger.LoggerPostRequest
import com.goiaba.data.models.logger.LoggerPostResponse
import com.goiaba.data.models.logger.LoggersResponse
import com.goiaba.data.services.logger.domain.LoggerRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoggerImpl : LoggerRepository {
    
    private val loggerService = LoggerService()
    private val uploadService = StrapiUploadService()
    
    override fun getLoggers(): Flow<RequestState<LoggersResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            delay(500)
            val result = loggerService.getLoggers()
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to fetch loggers: ${e.message}"))
        }
    }
    
    override suspend fun createLogger(request: LoggerPostRequest): Flow<RequestState<LoggerPostResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            delay(300)
            val result = loggerService.createLogger(request)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to create logger: ${e.message}"))
        }
    }
    
    override suspend fun getUploadedImages(): Flow<RequestState<List<UploadedImage>>> = flow {
        emit(RequestState.Loading)
        
        try {
            delay(200)
            val result = loggerService.getUploadedImages()
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to fetch images: ${e.message}"))
        }
    }

    override suspend fun uploadImageToStrapi(imageData: ByteArray, fileName: String): Flow<RequestState<UploadResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            delay(300)
            val result = uploadService.uploadImage(imageData, fileName)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to upload image: ${e.message}"))
        }
    }
}