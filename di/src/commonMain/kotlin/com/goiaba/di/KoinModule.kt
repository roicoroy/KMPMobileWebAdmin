package com.goiaba.di

import com.goiaba.data.services.adverts.AdvertImpl
import com.goiaba.data.services.adverts.domain.AdvertRepository
import com.goiaba.data.services.auth.LoginImpl
import com.goiaba.data.services.auth.RegisterImpl
import com.goiaba.data.services.auth.domain.LoginRepository
import com.goiaba.data.services.auth.domain.RegisterRepository
import com.goiaba.data.services.logger.LoggerImpl
import com.goiaba.data.services.logger.domain.LoggerRepository
import com.goiaba.data.services.profile.ProfileImpl
import com.goiaba.data.services.profile.domain.ProfileRepository
import com.goiaba.feature.AdvertsViewModel
import com.goiaba.feature.HomeGraphViewModel
import com.goiaba.feature.auth.login.LoginViewModel
import com.goiaba.feature.auth.register.RegisterViewModel
import com.goiaba.home.advert.details.AdvertDetailsViewModel
import com.goiaba.logger.LoggerViewModel
import com.goiaba.logger.details.LoggerDetailsViewModel
import com.goiaba.profile.ProfileViewModel
import com.goiaba.shared.util.IntentHandler
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<IntentHandler> { IntentHandler() }
    single<LoginRepository> { LoginImpl() }
    single<RegisterRepository> { RegisterImpl() }
    single<LoggerRepository> { LoggerImpl() }
    single<AdvertRepository> { AdvertImpl() }
    single<ProfileRepository> { ProfileImpl() }
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoggerViewModel)
    viewModelOf(::LoggerDetailsViewModel)
    viewModelOf(::AdvertsViewModel)
    viewModelOf(::AdvertDetailsViewModel)
    viewModelOf(::ProfileViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}