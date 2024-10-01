package com.punk.routes

import com.punk.exceptions.NoBeersFoundException
import com.punk.models.BeersRequest
import com.punk.services.BeerService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

private val logger = KotlinLogging.logger {}

fun Route.beerRouting(
    beerService: BeerService
) {
    route("/beers") {
        post {
            try {
                logger.info { "Searching for beers." }
                val request = call.receive<BeersRequest>()
                if(request.name != null) {
                    logger.info { "Searching for beers by name." }
                    call.respond(HttpStatusCode.OK, beerService.getBeersByName(request.name))
                } else {
                    logger.info { "Searching for beers." }
                    call.respond(HttpStatusCode.OK, beerService.getBeers(request.page))
                }
            } catch(e: NoBeersFoundException) {
                logger.error(e) { "No beers found." }
                call.respond(HttpStatusCode.NotFound, e.message)
            } catch(e: Exception) {
                logger.error(e) { "Request failed due to unspecified error." }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/{id}") {
            val request = call.parameters["id"]
            try {
                logger.info { "Searching for beer by ID: $request." }
                call.respond(HttpStatusCode.OK, beerService.getBeersById(request!!.toInt()))
            } catch(e: NumberFormatException) {
                logger.error(e) { "Given ID: $request failed to parse to integer" }
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: "Request failed to parse into Int data class: $request."
                )
            } catch(e: NoBeersFoundException) {
                logger.error(e) { "No beer found for given ID: $request." }
                call.respond(HttpStatusCode.NotFound, e.message)
            } catch(e: Exception) {
                logger.error(e) { "Request failed due to unspecified error." }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
