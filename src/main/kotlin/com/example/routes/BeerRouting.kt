package com.example.routes

import com.example.plugins.getAllBeers
import com.example.plugins.getBeerByID
import com.example.plugins.getBeerByName
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
            val query = call.request.queryString().split("=")

            // Checks if request is searching for specific beer name.
            if(query[0] == "beer_name"){
                runBlocking {
                    launch {
                        val beers = getBeerByName(query[1])
                        call.respond(HttpStatusCode.OK, beers)
                    }
                }
            }

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
            // Run Coroutine.
            runBlocking {
                launch {
                    val beer = getBeerByID(call.parameters["id"])
                    call.respond(HttpStatusCode.OK, beer)
                }
            }
        }
    }
}