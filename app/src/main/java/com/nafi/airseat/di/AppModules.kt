package com.nafi.airseat.di

import com.nafi.airseat.data.datasource.APIAuthDataSource
import com.nafi.airseat.data.datasource.AuthDataSource
import com.nafi.airseat.data.datasource.AuthService
import com.nafi.airseat.data.datasource.AuthServiceImpl
import com.nafi.airseat.data.datasource.seat.SeatApiDataSource
import com.nafi.airseat.data.datasource.seat.SeatDataSource
import com.nafi.airseat.data.network.services.AirSeatApiService
import com.nafi.airseat.data.repository.SeatRepository
import com.nafi.airseat.data.repository.SeatRepositoryImpl
import com.nafi.airseat.data.repository.UserRepository
import com.nafi.airseat.data.repository.UserRepositoryImpl
import com.nafi.airseat.presentation.biodata.OrdererBioViewModel
import com.nafi.airseat.presentation.biodata.PassengerBioViewModel
import com.nafi.airseat.presentation.home.HomeViewModel
import com.nafi.airseat.presentation.login.LoginViewModel
import com.nafi.airseat.presentation.otpaccount.OtpViewModel
import com.nafi.airseat.presentation.otpresetpassword.OtpResetPasswordViewModel
import com.nafi.airseat.presentation.register.RegisterViewModel
import com.nafi.airseat.presentation.resetpassword.ResetPasswordViewModel
import com.nafi.airseat.presentation.resetpasswordverifyemail.ReqChangePasswordViewModel
import com.nafi.airseat.presentation.seatbook.SeatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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
            single<SeatDataSource> { SeatApiDataSource(get()) }
        }

    private val repository =
        module {
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<SeatRepository> { SeatRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::OrdererBioViewModel)
            viewModelOf(::PassengerBioViewModel)
            viewModelOf(::HomeViewModel)
            viewModelOf(::SeatViewModel)

            viewModel {
                LoginViewModel(get())
            }
            viewModel {
                RegisterViewModel(get())
            }
            viewModel {
                OtpViewModel(get())
            }
            viewModel {
                OtpResetPasswordViewModel(get())
            }
            viewModel {
                ResetPasswordViewModel(get())
            }
            viewModel {
                ReqChangePasswordViewModel(get())
            }
        }

    val modules =
        listOf<Module>(networkModule, serviceModule, localModule, datasource, repository, viewModelModule)
}
