package com.nafi.airseat.di

import android.content.SharedPreferences
import com.nafi.airseat.core.BaseViewModel
import com.nafi.airseat.data.datasource.AuthDataSource
import com.nafi.airseat.data.datasource.AuthDataSourceImpl
import com.nafi.airseat.data.datasource.UserDataSource
import com.nafi.airseat.data.datasource.UserDataSourceImpl
import com.nafi.airseat.data.datasource.airport.AirportApiDataSource
import com.nafi.airseat.data.datasource.airport.AirportDataSource
import com.nafi.airseat.data.datasource.favoritedestination.FavoriteDestinationDataSource
import com.nafi.airseat.data.datasource.favoritedestination.FavoriteDestinationDataSourceImpl
import com.nafi.airseat.data.repository.AirportRepository
import com.nafi.airseat.data.repository.AirportRepositoryImpl
import com.nafi.airseat.data.repository.FavoriteDestinationRepository
import com.nafi.airseat.data.repository.FavoriteDestinationRepositoryImpl
import com.nafi.airseat.data.repository.PreferenceRepository
import com.nafi.airseat.data.repository.PreferenceRepositoryImpl
import com.nafi.airseat.data.repository.TokenRepository
import com.nafi.airseat.data.repository.TokenRepositoryImpl
import com.nafi.airseat.data.datasource.NotificationDataSource
import com.nafi.airseat.data.datasource.NotificationDataSourceImpl
import com.nafi.airseat.data.datasource.UserPrefDataSource
import com.nafi.airseat.data.datasource.UserPrefDataSourceImpl
import com.nafi.airseat.data.repository.NotificationRepository
import com.nafi.airseat.data.repository.NotificationRepositoryImpl
import com.nafi.airseat.data.repository.UserPrefRepository
import com.nafi.airseat.data.repository.UserPrefRepositoryImpl
import com.nafi.airseat.data.repository.UserRepository
import com.nafi.airseat.data.repository.UserRepositoryImpl
import com.nafi.airseat.data.source.local.pref.UserPreference
import com.nafi.airseat.data.source.local.pref.UserPreferenceImpl
import com.nafi.airseat.data.source.network.services.AirSeatApiService
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization
import com.nafi.airseat.data.source.network.services.TokenInterceptor
import com.nafi.airseat.presentation.biodata.OrdererBioViewModel
import com.nafi.airseat.presentation.biodata.PassengerBioViewModel
import com.nafi.airseat.presentation.home.HomeViewModel
import com.nafi.airseat.presentation.login.LoginViewModel
import com.nafi.airseat.presentation.notification.NotificationViewModel
import com.nafi.airseat.presentation.otpaccount.OtpViewModel
import com.nafi.airseat.presentation.otpresetpassword.OtpResetPasswordViewModel
import com.nafi.airseat.presentation.register.RegisterViewModel
import com.nafi.airseat.presentation.resetpassword.ResetPasswordViewModel
import com.nafi.airseat.presentation.resetpasswordverifyemail.ReqChangePasswordViewModel
import com.nafi.airseat.presentation.searchticket.SearchTicketViewModel
import com.nafi.airseat.utils.SharedPreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.scope.get
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
            single<AirSeatApiService> { AirSeatApiService.invoke() }
            single { TokenInterceptor(get()) }
            single<AirSeatApiServiceWithAuthorization> { AirSeatApiServiceWithAuthorization.invoke(get()) }
        }

    private val serviceModule =
        module {
        }

    private val localModule =
        module {
            single<SharedPreferences> {
                SharedPreferenceUtils.createPreference(
                    androidContext(),
                    UserPreferenceImpl.PREF_NAME,
                )
            }
            single<UserPreference> { UserPreferenceImpl(get()) }
        }

    private val datasource =
        module {
            single<AuthDataSource> { AuthDataSourceImpl(get()) }
            single<UserPrefDataSource> { UserPrefDataSourceImpl(get()) }
            single<NotificationDataSource> { NotificationDataSourceImpl(get()) }
            single<UserDataSource> { UserDataSourceImpl(get()) }
            single<AirportDataSource> { AirportApiDataSource(get()) }
            single<FavoriteDestinationDataSource> { FavoriteDestinationDataSourceImpl() }
        }

    private val repository =
        module {
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<TokenRepository> { TokenRepositoryImpl(androidContext(), get()) }
            single<PreferenceRepository> { PreferenceRepositoryImpl(get()) }
            single<AirportRepository> { AirportRepositoryImpl(get()) }
            single<FavoriteDestinationRepository> { FavoriteDestinationRepositoryImpl(get()) }
            single<UserPrefRepository> { UserPrefRepositoryImpl(get()) }
            single<NotificationRepository> { NotificationRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::OrdererBioViewModel)
            viewModelOf(::PassengerBioViewModel)
            viewModelOf(::HomeViewModel)
            viewModelOf(::LoginViewModel)
            viewModel {
                RegisterViewModel(get())
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
            viewModel {
                SearchTicketViewModel(get())
            }
            viewModel {
                NotificationViewModel(get())
            }
            viewModel {
                BaseViewModel(get())
            }
        }

    val modules =
        listOf<Module>(networkModule, serviceModule, localModule, datasource, repository, viewModelModule)
}
