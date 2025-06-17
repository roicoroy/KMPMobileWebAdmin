package com.goiaba.data.services.adverts.domain

import com.goiaba.data.models.adverts.AdvertGetResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface AdvertRepository {
    fun getAdverts(): Flow<RequestState<AdvertGetResponse>>
}