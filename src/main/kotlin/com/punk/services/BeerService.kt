package com.punk.services

import com.punk.exceptions.NoBeersFoundException
import com.punk.libraries.PunkClient
import com.punk.models.BeersResponse
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class BeerService(
    private val punkClient: PunkClient
) {

    suspend fun getBeers(page: Int? = null): BeersResponse {
        val beers = try {
            if(page != null)
                punkClient.getAllBeers(page)
            else
                punkClient.getAllBeers()
        } catch(e: Exception) {
            logger.error(e) { "PunkClient encountered unknown error." }
            throw e
        }

        if (beers.isEmpty()) {
            logger.error { "No beers returned in getBeers of page: $page." }
            throw NoBeersFoundException(function = "getBeers of page $page")
        }
        return BeersResponse(beers)
    }

    suspend fun getBeersByName(name: String): BeersResponse {
        val beers = try {
            punkClient.getBeersByName(name)
        } catch(e: Exception) {
            logger.error(e) { "PunkClient encountered unknown error." }
            throw e
        }

        if (beers.isEmpty()) {
            logger.error { "No beers returned in getBeersByName." }
            throw NoBeersFoundException(function = "getBeersByName $name")
        }

        return BeersResponse(beers)
    }

    suspend fun getBeersById(id: Int): BeersResponse {
        val beer = try {
            punkClient.getBeerByID(id)
        } catch(e: Exception) {
            logger.error(e) { "PunkClient encountered unknown error." }
            throw e
        }

        if (beer.isEmpty()) {
            logger.error { "No beer found with id: $id." }
            throw NoBeersFoundException(function = "getBeersById $id")
        }

        return BeersResponse(beer)
    }
}
