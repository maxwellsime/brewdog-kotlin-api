package com.punk.services

import com.punk.exceptions.BeerNotFoundException
import com.punk.exceptions.NoBeersFoundException
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
        val beers =
           if(page != null)
               punkClient.getAllBeers(page)
           else
               punkClient.getAllBeers()

        if (beers.isEmpty()) {
            logger.error { "No beers returned in getBeers of page: $page." }
            throw NoBeersFoundException(function = "getBeers of page $page")
        }
        return beers
    }

    // improve return errors when list of beers is empty
        // catch new exception inside routing, create exception function to turn into httpException
    // make output of api to be json
    // create tests
    suspend fun getBeersByName(name: String): List<Beer> {
        val beers = punkClient.getBeersByName(name)

        if (beers.isEmpty()) {
            logger.error { "No beers returned in getBeersByName." }
            throw NoBeersFoundException(function = "getBeersByName $name")
        }
        return beers
    }

    suspend fun getBeersById(id: Int): List<Beer> {
        val beer = punkClient.getBeerByID(id)

        if (beer.isEmpty()) {
            logger.error { "No beer found with id: $id." }
            throw BeerNotFoundException(beerId = id)
        }
        return beer
    }
}