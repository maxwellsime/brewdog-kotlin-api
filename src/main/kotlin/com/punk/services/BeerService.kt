package com.punk.services

import com.punk.libraries.PunkClient
import com.punk.models.Beer
import com.punk.models.ErrorMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

private val logger = KotlinLogging.logger {}

class BeerService(private val punkClient: PunkClient) {

    suspend fun getBeers(page: Int?): List<Beer> {
        // Checks if request is searching for specific beer page
        if(page != null){
            // Validation to check query[1] is a number and > 0.
            // UInt does not accept negative numbers
            if(request[1].toUIntOrNull() == null){
                val msg = ErrorMessage("${request[1]} is not a valid page number.")
                call.respond(status = HttpStatusCode.BadRequest, msg)
            }

            val beers = punkClient.getAllBeers(request[1])
            if(beers.isEmpty()){
                val msg = ErrorMessage("Page ${request[1]} does not exist.")
                call.respond(status = HttpStatusCode.NotFound, msg)
            } else {
                call.respond(HttpStatusCode.OK, beers)
            }
        }
        // improve return errors when list of beers is empty
        // make output of api to be json
        return punkClient.getAllBeers()
    }

    suspend fun getBeersByName(name: String): List<Beer> {
        val beers = punkClient.getBeerByName(request[1])

        if(beers.isEmpty()){
            val msg = ErrorMessage("No beer found with the name ${request[1]}")
            call.respond(status = HttpStatusCode.NotFound, msg)
        } else {
            call.respond(status = HttpStatusCode.OK, beers)
        }
    }

    suspend fun getBeersById(id: Int) {
        // Validation to make sure an id is entered.
        if(call.parameters["id"] != null){
            val id: String = call.parameters["id"].toString()

            // Validation to check id is a positive integer.
            if(id.toUIntOrNull() == null){
                val msg = ErrorMessage("$id is not a valid identifier. Identifiers must be short positive integers.")
                call.respond(status = HttpStatusCode.BadRequest, msg)
            }

            val beer = punkClient.getBeerByID(call.parameters["id"])

            if(beer.isEmpty()){
                val msg = ErrorMessage("Beer with the id: $id does not exist.")
                call.respond(status = HttpStatusCode.NotFound, msg)
            } else{
                call.respond(HttpStatusCode.OK, beer[0])
            }
        }
    }
}