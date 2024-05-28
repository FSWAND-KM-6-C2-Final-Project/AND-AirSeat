package com.nafi.airseat.di

import com.nafi.airseat.data.APIAuthDataSource
import com.nafi.airseat.data.AirSeatApiService
import com.nafi.airseat.data.AuthDataSource
import com.nafi.airseat.data.AuthService
import com.nafi.airseat.data.AuthServiceImpl
import com.nafi.airseat.data.UserRepository
import com.nafi.airseat.data.UserRepositoryImpl
import com.nafi.airseat.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import com.nafi.airseat.presentation.biodata.OrdererBioViewModel
import com.nafi.airseat.presentation.biodata.PassengerBioViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
            single<AirSeatApiService> { AirSeatApiService.invoke() }
        }

    private val serviceModule =
        module {
            single<AuthService> { AuthServiceImpl(get()) }
        }

    private val localModule =
        module {
        }

    private val datasource =
        module {
            single<AuthDataSource> { APIAuthDataSource(get()) }
        }

    private val repository =
        module {
            single<UserRepository> { UserRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::OrdererBioViewModel)
            viewModelOf(::PassengerBioViewModel)
            viewModel {
                LoginViewModel(get())
            }
        }

    val modules =
        listOf<Module>(networkModule, serviceModule, localModule, datasource, repository, viewModelModule)
}
