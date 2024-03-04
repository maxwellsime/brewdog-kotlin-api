package com.punk.routes

import com.punk.libraries.PunkClient
import com.punk.models.ErrorMessage
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

val punkClient = PunkClient()

// Routing for beer information using the PUNKAPI to make API calls.
// Responds with JSON with 200 OK or JSON filled with error message and relevant errorcode.
fun Route.beerRouting(){
    route("/beers"){
        // Get data for a list of beers.
        get {
            val query = call.request.queryString().split("=")

            // Checks if request is searching for specific beer name.
            if(query[0] == "beer_name" && query[1].isNotEmpty()){
                val beers = punkClient.getBeerByName(query[1])

                if(beers.isEmpty()){
                    val msg = ErrorMessage("No beer found with the name ${query[1]}")
                    call.respond(status = HttpStatusCode.NotFound, msg)
                } else {
                    call.respond(status = HttpStatusCode.OK, beers)
                }
            }

            // Checks if request is searching for specific beer page
            if(query[0] == "page"){
                // Validation to check query[1] is a number and > 0.
                // UInt does not accept negative numbers
                if(query[1].toUIntOrNull() == null){
                    val msg = ErrorMessage("${query[1]} is not a valid page number.")
                    call.respond(status = HttpStatusCode.BadRequest, msg)
                }

                val beers = punkClient.getAllBeers(query[1])
                if(beers.isEmpty()){
                    val msg = ErrorMessage("Page ${query[1]} does not exist.")
                    call.respond(status = HttpStatusCode.NotFound, msg)
                } else {
                    call.respond(HttpStatusCode.OK, beers)
                }
            }

            val beers = punkClient.getAllBeers()
            call.respond(HttpStatusCode.OK, beers)
        }
        // Get data for specific beer id.
        get("/{id}") {
            // Validation to make sure an id is entered.
            if(call.parameters["id"] != null){
                val id: String = call.parameters["id"].toString()

                // Validation to check id is a positive integer.
                if(id.toUIntOrNull() == null){
                    val msg = ErrorMessage("$id is not a valid identifier. Identifiers must be short positive integers.")
                    call.respond(status = HttpStatusCode.BadRequest, msg)
                }

                val beer = punkClient.etBeerByID(call.parameters["id"])

                if(beer.isEmpty()){
                    val msg = ErrorMessage("Beer with the id: $id does not exist.")
                    call.respond(status = HttpStatusCode.NotFound, msg)
                } else{
                    call.respond(HttpStatusCode.OK, beer[0])
                }
            }
        }
    }
}