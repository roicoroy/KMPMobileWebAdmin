package com.goiaba.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.goiaba.adverts.AdvertsScreen
import com.goiaba.feature.HomeGraphScreen
import com.goiaba.feature.auth.login.LoginScreen
import com.goiaba.feature.auth.register.RegisterScreen
import com.goiaba.home.advert.details.AdvertDetailsScreen
import com.goiaba.profile.ProfileAddressListScreen
import com.goiaba.profile.ProfileAdvertsListScreen
import com.goiaba.profile.ProfileScreen
import com.goiaba.shared.navigation.Screen

@Composable
fun SetupNavGraph(startDestination: Screen = Screen.HomeGraph) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToProfile = {
                    navController.navigate(Screen.ProfileScreen) {
                        popUpTo(Screen.ProfileScreen) {
                            inclusive = true
                        }
                    }
                },
                navigateToLogin = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.LoginScreen) {
                            inclusive = true
                        }
                    }
                },
                navigateToAdvertScreen = {
                    navController.navigate(Screen.AdvertsScreen) {
                        popUpTo(Screen.AdvertsScreen) {
                            inclusive = true
                        }
                    }
                },
                navigateToAdvertDetails = { advertId ->
                    navController.navigate(Screen.AdvertDetails(id = advertId))
                }
            )
        }
        composable<Screen.ProfileScreen> {
            ProfileScreen(
                navigateToAdvertsListScreen = {
                    navController.navigate(Screen.AdvertsListScreen) {
                        popUpTo(Screen.AdvertsListScreen) {
                            inclusive = true
                        }
                    }
                },
                navigateToAddressListScreen = {
                    navController.navigate(Screen.AddressListScreen) {
                        popUpTo(Screen.AddressListScreen) {
                            inclusive = true
                        }
                    }
                },
                navigateBack = {
                    navController.popBackStack()
                },
            )
        }
        composable<Screen.AdvertsListScreen> {
            ProfileAdvertsListScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo(Screen.HomeGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable<Screen.AddressListScreen> {
            ProfileAddressListScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo(Screen.HomeGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable<Screen.LoginScreen> {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo(Screen.HomeGraph) {
                            inclusive = true
                        }
                    }
                },
                navigateToRegister = {
                    navController.navigate(Screen.RegisterScreen)
                }
            )
        }
        composable<Screen.RegisterScreen> {
            RegisterScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo(Screen.HomeGraph) {
                            inclusive = true
                        }
                    }
                },
                navigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.AdvertsScreen> {
            AdvertsScreen(
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToAdvertDetails = { advertId ->
                    navController.navigate(Screen.AdvertDetails(id = advertId))
                }
            )
        }
        composable<Screen.AdvertDetails> {
            AdvertDetailsScreen(
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo(Screen.HomeGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}