package com.punk.fixtures

import com.punk.exceptions.NoBeersFoundException
import com.punk.models.Beer
import com.punk.models.BeersResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object BeerTestFixtures {

    const val ID = 1
    const val BEER_NAME = "beer name"
    private const val BEER_DESCRIPTION = "beer description"

    private val genericBeer = Beer(ID, BEER_NAME, BEER_DESCRIPTION)
    val genericBeerList = listOf(genericBeer)
    val genericBeerListJson = Json.encodeToString(genericBeerList)
    val getBeersGenericResponse = BeersResponse(listOf(genericBeer))
    val getBeersGenericResponseJson = Json.encodeToString(getBeersGenericResponse)

    val noBeersFoundException = NoBeersFoundException("getBeersByName $BEER_NAME")
}
