package com.c2.airseat.di

import android.content.SharedPreferences
import com.c2.airseat.core.BaseViewModel
import com.c2.airseat.data.datasource.AirportApiDataSource
import com.c2.airseat.data.datasource.AirportDataSource
import com.c2.airseat.data.datasource.AirportHistoryDataSource
import com.c2.airseat.data.datasource.AirportHistoryDataSourceImpl
import com.c2.airseat.data.datasource.AuthDataSource
import com.c2.airseat.data.datasource.AuthDataSourceImpl
import com.c2.airseat.data.datasource.BookingApiDataSource
import com.c2.airseat.data.datasource.BookingDataSource
import com.c2.airseat.data.datasource.FlightApiDataSource
import com.c2.airseat.data.datasource.FlightDataSource
import com.c2.airseat.data.datasource.FlightDetailApiDataSource
import com.c2.airseat.data.datasource.FlightDetailDataSource
import com.c2.airseat.data.datasource.HistoryDataSource
import com.c2.airseat.data.datasource.HistoryDataSourceImpl
import com.c2.airseat.data.datasource.IntroDataSource
import com.c2.airseat.data.datasource.IntroDataSourceImpl
import com.c2.airseat.data.datasource.NotificationDataSource
import com.c2.airseat.data.datasource.NotificationDataSourceImpl
import com.c2.airseat.data.datasource.ProfileDataSource
import com.c2.airseat.data.datasource.ProfileDataSourceImpl
import com.c2.airseat.data.datasource.SearchHistoryDataSource
import com.c2.airseat.data.datasource.SearchHistoryDataSourceImpl
import com.c2.airseat.data.datasource.SeatApiDataSource
import com.c2.airseat.data.datasource.SeatDataSource
import com.c2.airseat.data.datasource.UserPrefDataSource
import com.c2.airseat.data.datasource.UserPrefDataSourceImpl
import com.c2.airseat.data.repository.AirportHistoryRepository
import com.c2.airseat.data.repository.AirportHistoryRepositoryImpl
import com.c2.airseat.data.repository.AirportRepository
import com.c2.airseat.data.repository.AirportRepositoryImpl
import com.c2.airseat.data.repository.BookingRepository
import com.c2.airseat.data.repository.BookingRepositoryImpl
import com.c2.airseat.data.repository.FlightDetailRepository
import com.c2.airseat.data.repository.FlightDetailRepositoryImpl
import com.c2.airseat.data.repository.FlightRepository
import com.c2.airseat.data.repository.FlightRepositoryImpl
import com.c2.airseat.data.repository.HistoryRepository
import com.c2.airseat.data.repository.HistoryRepositoryImpl
import com.c2.airseat.data.repository.IntroRepository
import com.c2.airseat.data.repository.IntroRepositoryImpl
import com.c2.airseat.data.repository.NotificationRepository
import com.c2.airseat.data.repository.NotificationRepositoryImpl
import com.c2.airseat.data.repository.ProfileRepository
import com.c2.airseat.data.repository.ProfileRepositoryImpl
import com.c2.airseat.data.repository.SearchHistoryRepository
import com.c2.airseat.data.repository.SearchHistoryRepositoryImpl
import com.c2.airseat.data.repository.SeatRepository
import com.c2.airseat.data.repository.SeatRepositoryImpl
import com.c2.airseat.data.repository.UserPrefRepository
import com.c2.airseat.data.repository.UserPrefRepositoryImpl
import com.c2.airseat.data.repository.UserRepository
import com.c2.airseat.data.repository.UserRepositoryImpl
import com.c2.airseat.data.source.local.database.AppDatabase
import com.c2.airseat.data.source.local.database.dao.AirportHistoryDao
import com.c2.airseat.data.source.local.database.dao.SearchHistoryDao
import com.c2.airseat.data.source.local.pref.UserPreference
import com.c2.airseat.data.source.local.pref.UserPreferenceImpl
import com.c2.airseat.data.source.network.services.AirSeatApiService
import com.c2.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization
import com.c2.airseat.data.source.network.services.TokenInterceptor
import com.c2.airseat.presentation.appintro.AppIntroViewModel
import com.c2.airseat.presentation.biodata.OrdererBioViewModel
import com.c2.airseat.presentation.biodata.PassengerBioViewModel
import com.c2.airseat.presentation.deleteaccount.DeleteAccountViewModel
import com.c2.airseat.presentation.detailflight.DetailFlightViewModel
import com.c2.airseat.presentation.flightdetail.FlightDetailPriceViewModel
import com.c2.airseat.presentation.history.HistoryViewModel
import com.c2.airseat.presentation.home.HomeViewModel
import com.c2.airseat.presentation.login.LoginViewModel
import com.c2.airseat.presentation.notification.NotificationViewModel
import com.c2.airseat.presentation.otpaccount.OtpViewModel
import com.c2.airseat.presentation.otpresetpassword.OtpResetPasswordViewModel
import com.c2.airseat.presentation.profile.ProfileViewModel
import com.c2.airseat.presentation.register.RegisterViewModel
import com.c2.airseat.presentation.resetpassword.ResetPasswordViewModel
import com.c2.airseat.presentation.resetpasswordverifyemail.ReqChangePasswordViewModel
import com.c2.airseat.presentation.resultsearch.ResultSearchViewModel
import com.c2.airseat.presentation.resultsearchreturn.ResultSearchReturnViewModel
import com.c2.airseat.presentation.searchticket.SearchTicketViewModel
import com.c2.airseat.presentation.searcthistory.SearchHistoryViewModel
import com.c2.airseat.presentation.seatbook.SeatViewModel
import com.c2.airseat.presentation.seatbookreturn.SeatReturnViewModel
import com.c2.airseat.presentation.splashscreen.SplashScreenViewModel
import com.c2.airseat.presentation.updateprofile.UpdateProfileViewModel
import com.c2.airseat.utils.SharedPreferenceUtils
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
            single<AirSeatApiService> { AirSeatApiService.invoke() }
            single { TokenInterceptor(get()) }
            single<AirSeatApiServiceWithAuthorization> {
                AirSeatApiServiceWithAuthorization.invoke(
                    get(),
                )
            }
        }

    private val serviceModule =
        module {
        }

    private val localModule =
        module {
            single<AppDatabase> { AppDatabase.getInstance(androidContext()) }
            single<SharedPreferences> {
                SharedPreferenceUtils.createPreference(
                    androidContext(),
                    UserPreferenceImpl.PREF_NAME,
                )
            }
            single<UserPreference> { UserPreferenceImpl(get()) }
            single<SearchHistoryDao> { get<AppDatabase>().searchHistoryDao() }
            single<AirportHistoryDao> { get<AppDatabase>().airportHistoryDao() }
        }

    private val datasource =
        module {
            single<SeatDataSource> { SeatApiDataSource(get()) }
            single<AuthDataSource> { AuthDataSourceImpl(get()) }
            single<UserPrefDataSource> { UserPrefDataSourceImpl(get()) }
            single<NotificationDataSource> { NotificationDataSourceImpl(get()) }
            single<BookingDataSource> { BookingApiDataSource(get()) }
            single<AirportDataSource> { AirportApiDataSource(get()) }
            single<FlightDataSource> { FlightApiDataSource(get()) }
            single<FlightDetailDataSource> { FlightDetailApiDataSource(get()) }
            single<ProfileDataSource> { ProfileDataSourceImpl(get()) }
            single<HistoryDataSource> { HistoryDataSourceImpl(get()) }
            single<IntroDataSource> { IntroDataSourceImpl(get()) }
            single<SearchHistoryDataSource> { SearchHistoryDataSourceImpl(get()) }
            single<AirportHistoryDataSource> { AirportHistoryDataSourceImpl(get()) }
        }

    private val repository =
        module {
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<SeatRepository> { SeatRepositoryImpl(get()) }
            single<AirportRepository> { AirportRepositoryImpl(get()) }
            single<FlightRepository> { FlightRepositoryImpl(get()) }
            single<FlightDetailRepository> { FlightDetailRepositoryImpl(get()) }
            single<UserPrefRepository> { UserPrefRepositoryImpl(get()) }
            single<NotificationRepository> { NotificationRepositoryImpl(get()) }
            single<HistoryRepository> { HistoryRepositoryImpl(get()) }
            single<ProfileRepository> { ProfileRepositoryImpl(get()) }
            single<IntroRepository> { IntroRepositoryImpl(get()) }
            single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
            single<AirportHistoryRepository> { AirportHistoryRepositoryImpl(get()) }
            single<BookingRepository> { BookingRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::OrdererBioViewModel)
            viewModelOf(::PassengerBioViewModel)
            viewModelOf(::HomeViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::FlightDetailPriceViewModel)
            viewModelOf(::SeatViewModel)
            viewModelOf(::SeatReturnViewModel)
            viewModelOf(::AppIntroViewModel)
            viewModelOf(::SplashScreenViewModel)
            viewModelOf(::DeleteAccountViewModel)
            viewModelOf(::RegisterViewModel)
            viewModelOf(::OtpViewModel)
            viewModelOf(::OtpResetPasswordViewModel)
            viewModelOf(::ResetPasswordViewModel)
            viewModelOf(::ReqChangePasswordViewModel)
            viewModelOf(::SearchTicketViewModel)
            viewModelOf(::ResultSearchViewModel)
            viewModelOf(::ResultSearchReturnViewModel)
            viewModelOf(::DetailFlightViewModel)
            viewModelOf(::NotificationViewModel)
            viewModelOf(::BaseViewModel)
            viewModelOf(::HistoryViewModel)
            viewModelOf(::ProfileViewModel)
            viewModelOf(::SearchHistoryViewModel)
            viewModelOf(::UpdateProfileViewModel)
        }

    val modules =
        listOf<Module>(
            networkModule,
            serviceModule,
            localModule,
            datasource,
            repository,
            viewModelModule,
        )
}
