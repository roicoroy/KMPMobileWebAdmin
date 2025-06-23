package com.goiaba.data.models.adverts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdvertUpdateRequest(
    @SerialName("data")
    val data: AdvertUpdateData
) {
    @Serializable
    data class AdvertUpdateData(
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String,
        @SerialName("slug")
        val slug: String? = null,
        @SerialName("category")
        val category: String? = null, // Category document ID
        @SerialName("cover")
        val cover: String? = null // Image document ID
    )
}

@Serializable
data class AdvertUpdateResponse(
    @SerialName("data")
    val data: AdvertUpdateResponseData,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class AdvertUpdateResponseData(
        @SerialName("id")
        val id: Int,
        @SerialName("documentId")
        val documentId: String,
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String,
        @SerialName("slug")
        val slug: String? = null,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("updatedAt")
        val updatedAt: String,
        @SerialName("publishedAt")
        val publishedAt: String
    )

    @Serializable
    class Meta
}