package com.goiaba.data.models.adverts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdvertCreateRequest(
    @SerialName("data")
    val data: AdvertCreateData
) {
    @Serializable
    data class AdvertCreateData(
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String,
        @SerialName("slug")
        val slug: String? = null,
        @SerialName("category")
        val category: List<String>, // Category document ID
        @SerialName("cover")
        val cover: String? = null, // Image document ID
        @SerialName("user")
        val user: String // User document ID
    )
}
