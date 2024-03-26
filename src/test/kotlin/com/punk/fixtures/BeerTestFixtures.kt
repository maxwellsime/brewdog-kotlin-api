package com.punk.fixtures

import com.punk.models.Beer
import com.punk.models.BeersResponse

object BeerTestFixtures {

    const val ID = 1
    private const val BEER_NAME = "beer name"
    private const val BEER_DESCRIPTION = "beer description"


    private val getBeersGenericBeers = Beer(ID, BEER_NAME, BEER_DESCRIPTION)
    val getBeersGenericResponse = BeersResponse(listOf(getBeersGenericBeers))
}
