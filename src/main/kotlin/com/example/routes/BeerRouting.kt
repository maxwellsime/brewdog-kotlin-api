package com.example.routes

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun Route.beerRouting(){
    route("/beers"){
        // Get data for a list of beers.
        get {
            // Respond with data from Punk API
        }
    }
}