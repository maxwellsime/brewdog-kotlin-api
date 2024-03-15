package com.punk.modules

import com.punk.libraries.PunkClient
import com.punk.services.BeerService
import org.koin.dsl.module

val punkModule = module {

    single<PunkClient> { PunkClient() }

    single<BeerService> { BeerService(get()) }
}