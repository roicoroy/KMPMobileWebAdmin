package com.goiaba.data.services.adverts

import com.goiaba.data.models.adverts.AdvertCreateRequest
import com.goiaba.data.models.adverts.AdvertCreateResponse
import com.goiaba.data.models.adverts.AdvertGetResponse
import com.goiaba.data.models.adverts.AdvertUpdateRequest
import com.goiaba.data.models.adverts.AdvertUpdateResponse
import com.goiaba.data.models.adverts.CategoryResponse
import com.goiaba.data.models.profile.PutAddressToProfileResponse
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
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

    override fun getCategories(): Flow<RequestState<CategoryResponse>> = flow {
        emit(RequestState.Loading)

        try {
            delay(300)
            val result = advertService.getCategories()
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to fetch categories: ${e.message}"))
        }
    }

    override suspend fun createAdvert(request: AdvertCreateRequest): Flow<RequestState<AdvertCreateResponse>> = flow {
        emit(RequestState.Loading)

        try {
            delay(500)
            val result = advertService.createAdvert(request)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to create advert: ${e.message}"))
        }
    }

    override suspend fun addAdvertToProfile(
        profile: StrapiProfile,
        advertId: Int
    ): Flow<RequestState<PutAddressToProfileResponse>> = flow {
        emit(RequestState.Loading)
        try {
            delay(300) // Simulate latency.
            val result = advertService.addAdvertToProfile(profile, advertId)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to link user to address: ${e.message}"))
        }
    }

    override suspend fun updateAdvert(
        advertId: String,
        request: AdvertUpdateRequest
    ): Flow<RequestState<AdvertUpdateResponse>> = flow {
        emit(RequestState.Loading)

        try {
            delay(500)
            val result = advertService.updateAdvert(advertId, request)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to update advert: ${e.message}"))
        }
    }

    override suspend fun deleteAdvert(advertId: String): Flow<RequestState<Boolean>> = flow {
        emit(RequestState.Loading)

        try {
            delay(300)
            val result = advertService.deleteAdvert(advertId)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to delete advert: ${e.message}"))
        }
    }
}