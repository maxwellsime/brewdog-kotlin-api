package com.punk.services

import com.punk.exceptions.BeerNotFoundException
import com.punk.exceptions.NoBeersFoundException
import com.punk.fixtures.BeerTestFixtures
import com.punk.libraries.PunkClient
import com.punk.models.BeersResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class BeerServiceTests : ShouldSpec({

    val mockPunkClient = mockk<PunkClient>()
    val beerService = BeerService(mockPunkClient)

    afterTest {
        clearAllMocks()
    }

    context("getBeers") {
        should("Return a page of beers when given a valid input") {
            // GIVEN
            val expectedResponse = BeersResponse(BeerTestFixtures.genericBeerList)
            coEvery {
                mockPunkClient.getAllBeers()
            } returns BeerTestFixtures.genericBeerList

            // WHEN
            val response = beerService.getBeers()

            // THEN
            response shouldBe expectedResponse
            coVerify { mockPunkClient.getAllBeers() }
        }
        should("Throw an error when the page number is invalid") {
            // GIVEN
            val request = 10000
            coEvery {
                mockPunkClient.getAllBeers(request)
            } returns listOf()

            // WHEN
            shouldThrow<NoBeersFoundException> { beerService.getBeers(request) }

            // THEN
            coVerify { mockPunkClient.getAllBeers(request) }
        }
        should("Throw an error when the punk client encounters error") {
            // GIVEN
            coEvery {
                mockPunkClient.getAllBeers()
            } throws Exception()

            // WHEN
            shouldThrow<Exception> { beerService.getBeers() }

            // THEN
            coVerify { mockPunkClient.getAllBeers() }
        }
    }

    context("getBeersByName") {
        val request = "name"

        should("Return a beer when given a valid name") {
            // GIVEN
            val expectedResponse = BeersResponse(BeerTestFixtures.genericBeerList)
            coEvery {
                mockPunkClient.getBeersByName(request)
            } returns BeerTestFixtures.genericBeerList

            // WHEN
            val response = beerService.getBeersByName(request)

            // THEN
            response shouldBe expectedResponse
            coVerify { mockPunkClient.getBeersByName(request) }
        }
        should("Throw an exception when no beer found with given name") {
            // GIVEN
            coEvery {
                mockPunkClient.getBeersByName(request)
            } returns listOf()

            // WHEN
            shouldThrow<NoBeersFoundException> { beerService.getBeersByName(request) }

            // THEN
            coVerify { mockPunkClient.getBeersByName(request) }
        }
        should("Throw an exception when punk client encounters error") {
            // GIVEN
            coEvery {
                mockPunkClient.getBeersByName(request)
            } throws Exception()

            // WHEN
            shouldThrow<Exception> { beerService.getBeersByName(request) }

            // THEN
            coVerify { mockPunkClient.getBeersByName(request) }
        }
    }

    context("getBeersByID") {
        val request = 1

        should("Return a beer of the given ID") {
            // GIVEN
            coEvery {
                mockPunkClient.getBeerByID(request)
            } returns BeerTestFixtures.genericBeerList

            // WHEN
            val response = beerService.getBeersById(request)

            // THEN
            response.beers shouldBe BeerTestFixtures.genericBeerList
            request shouldBe response.beers.first().id
            coVerify { mockPunkClient.getBeerByID(request) }
        }
        should("Throws an BeerNotFoundException when punk client responds with no beers") {
            // GIVEN
            coEvery {
                mockPunkClient.getBeerByID(request)
            } returns listOf()

            // WHEN
            shouldThrow<BeerNotFoundException> { beerService.getBeersById(request) }

            // THEN
            coVerify { mockPunkClient.getBeerByID(request) }
        }
        should("Throws an exception when punk client encounters an error") {
            coEvery {
                mockPunkClient.getBeerByID(request)
            } throws Exception()

            // WHEN
            shouldThrow<Exception> { beerService.getBeersById(request) }

            // THEN
            coVerify { mockPunkClient.getBeerByID(request) }
        }
    }
})
