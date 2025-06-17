package com.goiaba.data.models.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressCreateRequest(
    @SerialName("data")
    val data: AddressCreateData
) {
    @Serializable
    data class AddressCreateData(
        @SerialName("first_name")
        val firstName: String,
        @SerialName("last_name")
        val lastName: String,
        @SerialName("first_line_address")
        val firstLineAddress: String,
        @SerialName("second_line_address")
        val secondLineAddress: String? = null,
        @SerialName("post_code")
        val postCode: String,
        @SerialName("city")
        val city: String? = null,
        @SerialName("country")
        val country: String? = null,
        @SerialName("phone_number")
        val phoneNumber: String? = null
        // Removed user field - we'll handle the association separately via user update
    )
}

@Serializable
data class AddressCreateResponse(
    @SerialName("data")
    val data: AddressCreateResponseData,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class AddressCreateResponseData(
        @SerialName("id")
        val id: Int,
        @SerialName("documentId")
        val documentId: String,
        @SerialName("first_name")
        val firstName: String,
        @SerialName("last_name")
        val lastName: String,
        @SerialName("first_line_address")
        val firstLineAddress: String,
        @SerialName("second_line_address")
        val secondLineAddress: String? = null,
        @SerialName("post_code")
        val postCode: String,
        @SerialName("city")
        val city: String? = null,
        @SerialName("country")
        val country: String? = null,
        @SerialName("phone_number")
        val phoneNumber: String? = null,
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