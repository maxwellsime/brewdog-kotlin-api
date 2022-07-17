package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import com.example.routes.beerRouting

fun Application.configureRouting() {
    routing {
        beerRouting()
    }
}
