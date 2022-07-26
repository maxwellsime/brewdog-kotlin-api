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

// Routing for beer information using the PUNKAPI to make API calls.
// Responds with JSON with 200 OK or JSON filled with error message and relevant errorcode.
fun Route.beerRouting(){
    route("/beers"){
        // Get data for a list of beers.
        get {
            val query = call.request.queryString().split("=")

            // Checks if request is searching for specific beer name.
            if(query[0] == "beer_name" && query[1].isNotEmpty()){
                runBlocking {
                    launch {
                        val beers = getBeerByName(query[1])

                        if(beers.isEmpty()){
                            call.respond(status = HttpStatusCode.NotFound,"No beers with names including: ${query[1]}.")
                        } else {
                            call.respond(status = HttpStatusCode.OK, beers)
                        }
                    }
                }
            }

            // Checks if request is searching for specific beer page
            if(query[0] == "page"){
                // Validation to check query[1] is a number and > 0.
                // UInt does not accept negative numbers
                if(query[1].toUIntOrNull() == null){
                    call.respond(status = HttpStatusCode.BadRequest, "${query[1]} is not a valid page number.")
                }

                runBlocking {
                    launch {
                        val beers = getAllBeers(query[1])

                        if(beers.isEmpty()){
                            call.respond(status = HttpStatusCode.NotFound, "Page ${query[1]} does not exist.")
                        } else{
                            call.respond(HttpStatusCode.OK, beers)
                        }
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
            // Validation to make sure an id is entered.
            if(call.parameters["id"] != null){
                var id: String = call.parameters["id"].toString()

                // Validation to check id is a positive integer.
                if(id.toUIntOrNull() == null){
                    call.respond(status = HttpStatusCode.BadRequest, "$id is not a valid identifier. Identifiers must be short positive integers.")
                }

                // Run Coroutine.
                runBlocking {
                    launch {
                        val beer = getBeerByID(call.parameters["id"])

                        if(beer.isEmpty()){
                            call.respond(status = HttpStatusCode.NotFound, "Beer with the id: $id does not exist.")
                        } else{
                            call.respond(HttpStatusCode.OK, beer[0])
                        }
                    }
                }
            }
        }
    }
}