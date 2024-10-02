package com.punk.modules

import com.punk.libraries.PunkClient
import com.punk.services.BeerService
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import org.koin.dsl.module

val punkModule = module {

    val apiUrl = HoconApplicationConfig(ConfigFactory.load()).config("punk").property("url").getString()

    single<PunkClient> { PunkClient(apiUrl) }

    single<BeerService> { BeerService(get()) }
}
