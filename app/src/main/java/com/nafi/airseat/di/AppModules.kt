package com.nafi.airseat.di

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
        }

    val modules =
        listOf<Module>(networkModule, firebaseModule, localModule, datasource, repository, viewModelModule)
}
