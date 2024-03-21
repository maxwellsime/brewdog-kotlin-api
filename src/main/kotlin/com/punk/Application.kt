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

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) { json() }
        install(Koin) { modules(punkModule) }

        val beerService by inject<BeerService>()

        configureRouting(beerService)
    }.start(wait = true)
}

fun Application.configureRouting(beerService: BeerService) {
    routing {
        beerRouting(beerService)
    }
}
