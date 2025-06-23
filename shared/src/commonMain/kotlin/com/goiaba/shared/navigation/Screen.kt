package com.goiaba.shared.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object HomeGraph : Screen()

    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object RegisterScreen : Screen()

    @Serializable
    data object PostsScreen : Screen()

    @Serializable
    data class Details(
        val id: String
    ) : Screen()

    @Serializable
    data object LoggerScreen : Screen()

    @Serializable
    data class LoggerDetails(
        val id: String
    ) : Screen()

    @Serializable
    data object AdvertsScreen : Screen()

    @Serializable
    data class AdvertDetails(
        val id: String
    ) : Screen()

    @Serializable
    data object ProfileScreen : Screen()

    @Serializable
    data object AdvertsListScreen : Screen()

    @Serializable
    data object AddressListScreen : Screen()
}