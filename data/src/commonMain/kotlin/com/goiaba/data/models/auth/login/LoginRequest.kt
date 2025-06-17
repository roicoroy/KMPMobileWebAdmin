package com.goiaba.data.models.auth.login


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "identifier": "test@test.com",
  "password": "Password"
}
*/
@Serializable
data class LoginRequest(
    @SerialName("identifier")
    val identifier: String,
    @SerialName("password")
    val password: String
)