package com.goiaba.data.models.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateRequest(
    @SerialName("data")
    val data: UserUpdateData
) {
    @Serializable
    data class UserUpdateData(
        @SerialName("addresses")
        val addresses: List<String>? = null, // List of address documentIds
        @SerialName("adverts")
        val adverts: List<String>? = null // List of advert documentIds
    )
}

@Serializable
data class UserUpdateResponse(
    @SerialName("data")
    val data: UserUpdateResponseData,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class UserUpdateResponseData(
        @SerialName("id")
        val id: Int,
        @SerialName("documentId")
        val documentId: String,
        @SerialName("username")
        val username: String,
        @SerialName("email")
        val email: String,
        @SerialName("provider")
        val provider: String,
        @SerialName("confirmed")
        val confirmed: Boolean,
        @SerialName("blocked")
        val blocked: Boolean,
        @SerialName("professional")
        val professional: Boolean,
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