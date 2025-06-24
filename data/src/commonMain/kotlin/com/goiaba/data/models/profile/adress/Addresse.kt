package com.goiaba.data.models.profile.adress


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "id": 1,
  "documentId": "r21y5s6f16brcbn6a2zwex0f",
  "username": "roicoroy",
  "email": "roicoroy@yahoo.com.br",
  "provider": "local",
  "confirmed": true,
  "blocked": false,
  "createdAt": "2025-06-11T13:47:53.411Z",
  "updatedAt": "2025-06-11T15:37:09.454Z",
  "publishedAt": "2025-06-11T15:37:09.449Z",
  "professional": true,
  "adverts": [
    {
      "id": 3,
      "documentId": "gotti3czw8dcaldyjoqsmya6",
      "title": "Massage",
      "description": "Massage Test one for the store example",
      "slug": "massage",
      "createdAt": "2025-06-11T15:09:51.061Z",
      "updatedAt": "2025-06-12T14:10:03.651Z",
      "publishedAt": "2025-06-12T14:10:03.642Z"
    }
  ],
  "addresses": [
    {
      "id": 1,
      "documentId": "jtxgmf3x7e7bcqhxbzhfno0n",
      "first_name": "Ricardo",
      "last_name": "Bento",
      "first_line_address": "Flat 26, 46 Newhaven Place",
      "second_line_address": null,
      "post_code": "EH64UH",
      "city": "Edinburgh",
      "country": "United Kingdom",
      "phone_number": "07510963961",
      "createdAt": "2025-06-13T07:44:51.370Z",
      "updatedAt": "2025-06-13T07:45:56.991Z",
      "publishedAt": "2025-06-13T07:45:56.983Z"
    }
  ]
}
*/
@Serializable
data class Addresse(
    @SerialName("city")
    val city: String?,
    @SerialName("country")
    val country: String?,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("documentId")
    val documentId: String,
    @SerialName("first_line_address")
    val firstLineAddress: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("id")
    val id: Int,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("phone_number")
    val phoneNumber: String?,
    @SerialName("post_code")
    val postCode: String,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("second_line_address")
    val secondLineAddress: String?,
    @SerialName("updatedAt")
    val updatedAt: String
)