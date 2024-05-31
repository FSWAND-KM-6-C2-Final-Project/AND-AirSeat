package com.nafi.airseat.di

import com.nafi.airseat.presentation.biodata.OrdererBioViewModel
import com.nafi.airseat.presentation.biodata.PassengerBioViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
        }

    private val firebaseModule =
        module {
        }

    private val localModule =
        module {
        }

    private val datasource =
        module {
        }

    private val repository =
        module {
        }

    private val viewModelModule =
        module {
            viewModelOf(::OrdererBioViewModel)
            viewModelOf(::PassengerBioViewModel)
        }

    val modules =
        listOf<Module>(networkModule, firebaseModule, localModule, datasource, repository, viewModelModule)
}
