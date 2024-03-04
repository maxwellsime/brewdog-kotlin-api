package com.punk.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import com.punk.routes.beerRouting

fun Application.configureRouting() {
    routing {
        beerRouting()
    }
}
