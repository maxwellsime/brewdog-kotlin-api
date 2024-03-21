package com.punk.modules

import com.punk.libraries.PunkClient
import com.punk.services.BeerService
import org.koin.dsl.module

val punkModule = module {

    val punkApiUrl = "https://api.punkapi.com/v2/"

    single<PunkClient> { PunkClient(punkApiUrl) }

    single<BeerService> { BeerService(get()) }
}