package com.punk.routes

import com.punk.fixtures.BeerTestFixtures
import com.punk.fixtures.BeerTestFixtures.getBeersGenericResponse
import com.punk.fixtures.BeerTestFixtures.getBeersGenericResponseJson
import com.punk.fixtures.BeerTestFixtures.noBeersFoundException
import com.punk.models.BeersRequest
import com.punk.services.BeerService
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BeerRoutingTest : ShouldSpec({

    val mockBeerService = mockk<BeerService>()

    afterTest {
        clearAllMocks()
    }

    context("POST /beers route") {
        should("Return a list of beer models from page 1 when given a name") {
            // GIVEN
            val requestName = "name"
            coEvery { mockBeerService.getBeersByName(requestName) } returns getBeersGenericResponse

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                post("/beers") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(Json.encodeToString(BeersRequest(requestName, null)))
                    accept(ContentType.Application.Json)
                }
            }

            // THEN
            with(response!!) {
                this.status shouldBe HttpStatusCode.OK
                this.bodyAsText() shouldBe getBeersGenericResponseJson
            }
            coVerify(exactly = 1) { mockBeerService.getBeersByName(requestName) }
        }
        should("Return a list of beer models from page 1 when given no request") {
            // GIVEN
            coEvery { mockBeerService.getBeers(null) } returns getBeersGenericResponse

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                post("/beers") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(Json.encodeToString(BeersRequest(null, null)))
                    accept(ContentType.Application.Json)
                }
            }

            // THEN
            with(response!!) {
                this.status shouldBe HttpStatusCode.OK
                this.bodyAsText() shouldBe getBeersGenericResponseJson
            }
            coVerify(exactly = 1) { mockBeerService.getBeers(null) }
        }
        should("Return NotFound when NoBeersFoundException thrown by service") {
            // GIVEN
            coEvery { mockBeerService.getBeers(null) } throws noBeersFoundException

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                post("/beers") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(Json.encodeToString(BeersRequest(null, null)))
                    accept(ContentType.Application.Json)
                }
            }

            // THEN
            response!!.status shouldBe HttpStatusCode.NotFound
            coVerify(exactly = 1) { mockBeerService.getBeers(null) }
        }
        should("Return InternalServerError when Exception thrown by service") {
            // GIVEN
            coEvery { mockBeerService.getBeers(null) } throws Exception()

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                post("/beers") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(Json.encodeToString(BeersRequest(null, null)))
                    accept(ContentType.Application.Json)
                }
            }

            // THEN
            response!!.status shouldBe HttpStatusCode.InternalServerError
            coVerify(exactly = 1) { mockBeerService.getBeers(null) }
        }
    }

    context("GET /beers/id route") {
        should("Return a beer of a specific ID") {
            // GIVEN
            coEvery { mockBeerService.getBeersById(BeerTestFixtures.ID) } returns getBeersGenericResponse

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                get("/beers/${BeerTestFixtures.ID}") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }

            // THEN
            with(response) {
                if(this != null) {
                    this.status shouldBe HttpStatusCode.OK
                }
                coVerify(exactly = 1) { mockBeerService.getBeersById(BeerTestFixtures.ID) }
            }
        }
        should("Return BadRequest when given ID is incorrect type") {
            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                get("/beers/x") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }

            // THEN
            with(response) {
                if(this != null) {
                    this.status shouldBe HttpStatusCode.BadRequest
                }
            }
        }
        should("Return NotFound when no beers have been found using given ID"){
            // GIVEN
            coEvery { mockBeerService.getBeersById(BeerTestFixtures.ID) } throws noBeersFoundException

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                get("/beers/${BeerTestFixtures.ID}") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }

            // THEN
            with(response) {
                if(this != null) {
                    this.status shouldBe HttpStatusCode.NotFound
                }
                coVerify(exactly = 1) { mockBeerService.getBeersById(BeerTestFixtures.ID) }
            }
        }
        should("Return InternalServerError when an unprepared exception occurs"){
            // GIVEN
            coEvery { mockBeerService.getBeersById(BeerTestFixtures.ID) } throws Exception()

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                get("/beers/${BeerTestFixtures.ID}") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }

            // THEN
            with(response) {
                if(this != null) {
                    this.status shouldBe HttpStatusCode.InternalServerError
                }
                coVerify(exactly = 1) { mockBeerService.getBeersById(BeerTestFixtures.ID) }
            }
        }
    }
})

fun withBaseTestApplication(
    mockBeerService: BeerService,
    request: suspend HttpClient.() -> HttpResponse
): HttpResponse? {
    var response: HttpResponse? = null

    testApplication{
        application {
            install(ContentNegotiation) {
                json(Json { isLenient = true })
            }
            routing { beerRouting(mockBeerService) }
        }

        response = client.request()
    }

    return response
}
