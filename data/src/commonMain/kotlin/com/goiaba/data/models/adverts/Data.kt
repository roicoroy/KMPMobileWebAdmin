package com.goiaba.data.models.adverts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "data": {
    "id": 5,
    "documentId": "bt7v7xsid2uhhux2gjb0qc5z",
    "title": "Hll",
    "description": "jdjdj",
    "slug": "Advert",
    "createdAt": "2025-06-23T22:00:47.247Z",
    "updatedAt": "2025-06-23T22:00:47.247Z",
    "publishedAt": "2025-06-23T22:00:47.244Z"
  },
  "meta": {}
}
*/
@Serializable
data class Data(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("description")
    val description: String,
    @SerialName("documentId")
    val documentId: String,
    @SerialName("id")
    val id: Int,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("title")
    val title: String,
    @SerialName("updatedAt")
    val updatedAt: String
)