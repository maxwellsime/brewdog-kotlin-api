package com.punk.routes

import com.punk.models.BeersRequest
import com.punk.models.ErrorMessage
import com.punk.services.BeerService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

private val logger = KotlinLogging.logger {}

// Routing for beer information using the PUNKAPI to make API calls.
// Responds with JSON with 200 OK or JSON filled with error message and relevant errorcode.
fun Route.beerRouting(beerService: BeerService) {
    route("/beers") {
        // Get data for a list of beers.
        get {
            try {
                val request = call.receive<BeersRequest>()
                if(request.name != null) {
                    logger.info { "Searching for beers by name" }
                    call.respond(HttpStatusCode.OK, beerService.getBeersByName(request.name))
                } else {
                    logger.info { "Searching for beers" }
                    call.respond(HttpStatusCode.OK, beerService.getBeers(request.page))
                }
            } catch(e: ContentTransformationException) {
                logger.error(e) { "Request failed to parse into BeersRequest data class" }
                call.respond(HttpStatusCode.BadRequest)
            } catch(e: Exception) {
                logger.error(e) { "Request failed due to unspecified error" }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/{id}") {
            try {
                val request = call.receive<Int>()
                call.respond(HttpStatusCode.OK, beerService.getBeersById(request))
            } catch(e: ContentTransformationException) {
                logger.error(e) { "Request failed to parse into BeersRequest data class" }
                call.respond(HttpStatusCode.BadRequest)
            } catch(e: Exception) {
                logger.error(e) { "Request failed due to unspecified error" }
                call.respond(HttpStatusCode.InternalServerError)
            }
    }
    }
}
