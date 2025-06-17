package com.goiaba.data.models.profile


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StrapiUser(
    @SerialName("addresses")
    val addresses: List<Addresse?> = listOf(),
    @SerialName("adverts")
    val adverts: List<Advert?> = listOf(),
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
    @SerialName("provider")
    val provider: String = "",
    @SerialName("publishedAt")
    val publishedAt: String = "",
    @SerialName("updatedAt")
    val updatedAt: String = "",
    @SerialName("username")
    val username: String = ""
)