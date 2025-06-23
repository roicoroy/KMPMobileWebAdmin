package com.goiaba.data.services.adverts.domain

import com.goiaba.data.models.adverts.AdvertCreateRequest
import com.goiaba.data.models.adverts.AdvertCreateResponse
import com.goiaba.data.models.adverts.AdvertGetResponse
import com.goiaba.data.models.adverts.AdvertUpdateRequest
import com.goiaba.data.models.adverts.AdvertUpdateResponse
import com.goiaba.data.models.adverts.CategoryResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface AdvertRepository {
    fun getAdverts(): Flow<RequestState<AdvertGetResponse>>
    fun getCategories(): Flow<RequestState<CategoryResponse>>
    suspend fun createAdvert(request: AdvertCreateRequest): Flow<RequestState<AdvertCreateResponse>>
    suspend fun updateAdvert(advertId: String, request: AdvertUpdateRequest): Flow<RequestState<AdvertUpdateResponse>>
    suspend fun deleteAdvert(advertId: String): Flow<RequestState<Boolean>>
}