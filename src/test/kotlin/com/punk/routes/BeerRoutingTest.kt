package com.punk.routes

import com.punk.fixtures.BeerTestFixtures
import com.punk.fixtures.BeerTestFixtures.getBeersGenericResponse
import com.punk.models.BeersRequest
import com.punk.models.BeersResponse
import com.punk.modules.punkModule
import com.punk.services.BeerService
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.*
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
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin

class BeerRoutingTest : ShouldSpec({

    val mockBeerService = mockk<BeerService>()

    afterTest {
        clearAllMocks()
    }

    context("post /beers route") {
        should("Return a list of beer models from page 1 when given no request") {
            // GIVEN
            coEvery { mockBeerService.getBeers(null) } returns getBeersGenericResponse

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                post("/beers") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(BeersRequest(null, null))
                }
            }

            // THEN
            with(response) {
                if(this != null) {
                    this.status shouldBe HttpStatusCode.OK
                    this.body<BeersResponse>() shouldBe getBeersGenericResponse
                }
                coVerify(exactly = 1) { mockBeerService.getBeers(null) }
            }
        }
    }

    context("get /beers/id route") {
        should("return a beer of a specific ID") {
            // GIVEN
            coEvery { mockBeerService.getBeersById(BeerTestFixtures.ID) } returns getBeersGenericResponse

            // WHEN
            val response = withBaseTestApplication(mockBeerService) {
                get("/beers/${BeerTestFixtures.ID}") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            }

            // THEN
            with(response) {
                if(this != null) {
                    this.status shouldBe HttpStatusCode.OK
                    this.body<BeersResponse>() shouldBe getBeersGenericResponse
                }
                coVerify(exactly = 1) { mockBeerService.getBeersById(BeerTestFixtures.ID) }
            }
        }
    }
})

fun withBaseTestApplication(
    mockBeerServer: BeerService,
    request: suspend HttpClient.() -> HttpResponse
): HttpResponse? {
    var response: HttpResponse? = null

    testApplication{
        application {
            install(ContentNegotiation) {
                json(Json { isLenient = true })
            }
            install(Koin) { modules(punkModule) }
            routing { beerRouting(mockBeerServer) }
        }

        response = client.request()
    }

    return response
}
