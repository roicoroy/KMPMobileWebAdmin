package com.goiaba.di

import com.goiaba.shared.util.IntentHandler
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val sharedModule = module {
    single<IntentHandler> { IntentHandler() }
//    single<LoginRepository> { LoginImpl() }
//    single<RegisterRepository> { RegisterImpl() }
//    single<LoggerRepository> { LoggerImpl() }
//    single<AdvertRepository> { AdvertImpl() }
//    single<ProfileRepository> { ProfileImpl() }
//    viewModelOf(::HomeGraphViewModel)
//    viewModelOf(::LoginViewModel)
//    viewModelOf(::RegisterViewModel)
//    viewModelOf(::LoggerViewModel)
//    viewModelOf(::LoggerDetailsViewModel)
//    viewModelOf(::AdvertsViewModel)
//    viewModelOf(::AdvertDetailsViewModel)
//    viewModelOf(::ProfileViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}