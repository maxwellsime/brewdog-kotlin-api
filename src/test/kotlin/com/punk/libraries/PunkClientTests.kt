package com.punk.libraries

import com.punk.fixtures.BeerTestFixtures.BEER_NAME
import com.punk.fixtures.BeerTestFixtures.ID
import com.punk.fixtures.BeerTestFixtures.genericBeerList
import com.punk.fixtures.PunkClientMockEngine.API_URL
import com.punk.fixtures.PunkClientMockEngine.INVALID_ID
import com.punk.fixtures.PunkClientMockEngine.INVALID_PAGE
import com.punk.fixtures.PunkClientMockEngine.PAGE
import com.punk.fixtures.PunkClientMockEngine.punkClientMockEngine
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class PunkClientTests : ShouldSpec({

    val client = PunkClient(API_URL, punkClientMockEngine)

    context("getAllBeers") {
        should("Returns a list of beers when no request") {
            // WHEN
            val response = client.getAllBeers()

            // THEN
            response shouldBe genericBeerList
        }
        should("Returns a list of beers when given a valid page") {
            // WHEN
            val response = client.getAllBeers(PAGE)

            // THEN
            response shouldBe genericBeerList
        }
        should("Returns empty list when given an invalid page") {
            // WHEN
            val response = client.getAllBeers(INVALID_PAGE)

            // THEN
            response shouldBe listOf()
        }
    }
    context("getBeerByID") {
        should("Returns a beer when given a valid ID") {
            // WHEN
            val response = client.getBeerByID(ID)

            // THEN
            response shouldBe genericBeerList
        }
        should("Return empty list when given invalid ID") {
            // WHEN
            val response = client.getBeerByID(INVALID_ID)

            // THEN
            response shouldBe listOf()
        }
    }
    context("getBeersByName") {
        should("Returns a beer by specific name") {
            // WHEN
            val response = client.getBeersByName(BEER_NAME)

            // THEN
            response shouldBe genericBeerList
        }
    }
})
