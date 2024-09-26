package com.punk.routes

import com.punk.exceptions.BeerNotFoundException
import com.punk.exceptions.NoBeersFoundException
import com.punk.models.BeersRequest
import com.punk.services.BeerService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.request.ContentTransformationException
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
            } catch(e: ContentTransformationException) {
                logger.error(e) { "Request failed to parse into BeersRequest data class." }
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: "Request failed to parse into BeerRequest data class"
                )
            } catch(e: NoBeersFoundException) {
                logger.error(e) { "No beers ." }
                call.respond(HttpStatusCode.NotFound, e.toNotFoundException())
            } catch(e: Exception) {
                logger.error(e) { "Request failed due to unspecified error." }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get("/{id}") {
            val request = call.receive<Int>()
            try {
                logger.info { "Searching for beer by ID: $request." }
                call.respond(HttpStatusCode.OK, beerService.getBeersById(request))
            } catch(e: ContentTransformationException) {
                logger.error(e) { "Request failed to parse into BeersRequest data class." }
                call.respond(
                    HttpStatusCode.BadRequest,
                    e.message ?: "Request failed to parse into Int data class"
                )
            } catch(e: BeerNotFoundException) {
                logger.error(e) { "No beer found for id: $request." }
                call.respond(HttpStatusCode.NotFound, e.toNotFoundException())
            } catch(e: Exception) {
                logger.error(e) { "Request failed due to unspecified error." }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}
