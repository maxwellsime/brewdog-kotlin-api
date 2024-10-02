package com.punk

import com.punk.modules.punkModule
import com.punk.routes.beerRouting
import com.punk.services.BeerService
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}

fun Application.main() {
    install(ContentNegotiation) { json() }
    install(Koin) { modules(punkModule) }

    val beerService by inject<BeerService>()

    configureRouting(beerService)
}

fun Application.configureRouting(beerService: BeerService) {
    routing {
        beerRouting(beerService)
    }
}
