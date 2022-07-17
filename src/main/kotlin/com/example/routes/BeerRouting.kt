package com.example.routes

import com.example.plugins.getAllBeers
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun Route.beerRouting(){
    route("/beers"){
        // Get data for a list of beers.
        get {
            // Runs Coroutine responding with list of all beers if no query.
            runBlocking {
                launch {
                    val beers = getAllBeers()
                    call.respond(HttpStatusCode.OK, beers)
                }
            }
        }
        // Get data for specific beer id.
        get("/{id}") {
        }
    }
}