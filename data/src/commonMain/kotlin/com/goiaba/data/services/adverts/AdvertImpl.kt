package com.goiaba.data.services.adverts

import com.goiaba.data.models.adverts.AdvertGetResponse
import com.goiaba.data.services.adverts.domain.AdvertRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AdvertImpl : AdvertRepository {
    
    private val advertService = AdvertService()
    
    override fun getAdverts(): Flow<RequestState<AdvertGetResponse>> = flow {
        emit(RequestState.Loading)
        
        try {
            delay(500)
            val result = advertService.getAdverts()
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to fetch adverts: ${e.message}"))
        }
    }
}