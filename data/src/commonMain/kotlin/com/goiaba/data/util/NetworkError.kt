package com.goiaba.data.util

enum class NetworkError : Error {
    REQUEST_TIMEOUT,
    UNAUTHORIZED,
    CONFLICT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    PAYLOAD_TOO_LARGE,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN,
    NOT_FOUND,
    BAD_REQUEST,
    FORBIDDEN
}

fun NetworkError.toMessage(): String {
    return when (this) {
        NetworkError.REQUEST_TIMEOUT -> "Request timed out. Please try again."
        NetworkError.UNAUTHORIZED -> "Unauthorized access. Please check your credentials."
        NetworkError.CONFLICT -> "Data conflict occurred. Please refresh and try again."
        NetworkError.TOO_MANY_REQUESTS -> "Too many requests. Please wait a moment."
        NetworkError.NO_INTERNET -> "No internet connection. Please check your network."
        NetworkError.PAYLOAD_TOO_LARGE -> "Request too large. Please reduce data size."
        NetworkError.SERVER_ERROR -> "Server error occurred. Please try again later."
        NetworkError.SERIALIZATION -> "Data parsing error. Please contact support."
        NetworkError.NOT_FOUND -> "Requested resource not found."
        NetworkError.BAD_REQUEST -> "Invalid request. Please check your input."
        NetworkError.FORBIDDEN -> "Access forbidden. You don't have permission."
        NetworkError.UNKNOWN -> "An unknown error occurred. Please try again."
    }
}