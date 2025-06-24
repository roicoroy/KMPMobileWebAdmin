package com.goiaba.data.models.profile.adress

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressUpdateRequest(
    @SerialName("data")
    val data: AddressData
) {
    @Serializable
    data class AddressData(
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
    )
}

@Serializable
data class AddressUpdateResponse(
    @SerialName("data")
    val data: AddressResponseData,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class AddressResponseData(
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