package com.goiaba.data.models.adverts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("data")
    val data: List<Category>,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class Category(
        @SerialName("id")
        val id: Int,
        @SerialName("documentId")
        val documentId: String,
        @SerialName("name")
        val name: String,
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
    data class Meta(
        @SerialName("pagination")
        val pagination: Pagination
    ) {
        @Serializable
        data class Pagination(
            @SerialName("page")
            val page: Int,
            @SerialName("pageCount")
            val pageCount: Int,
            @SerialName("pageSize")
            val pageSize: Int,
            @SerialName("total")
            val total: Int
        )
    }
}