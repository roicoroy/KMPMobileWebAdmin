package com.goiaba.data.models.profile.adress


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PutAddressResponse(
    @SerialName("blocked")
    val blocked: Boolean = false,
    @SerialName("confirmed")
    val confirmed: Boolean = false,
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("documentId")
    val documentId: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("professional")
    val professional: Boolean = false,
    @SerialName("provider")
    val provider: String = "",
    @SerialName("publishedAt")
    val publishedAt: String = "",
    @SerialName("updatedAt")
    val updatedAt: String = "",
    @SerialName("username")
    val username: String = ""
)