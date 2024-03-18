package com.punk.routes

import com.punk.configureRouting
import com.punk.models.BeersResponse
import com.punk.modules.punkModule
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.koin.ktor.plugin.Koin

class BeerRoutingTest : ShouldSpec({

    context("/beers route") {
        should("Return a list of beer models from page 1 when given no request") {
            // GIVEN
            testApplication {
                runBlocking {
                    application {
                        install(ContentNegotiation){
                            json()
                        }

                        configureRouting()
                    }
                }

                // WHEN
                val response = withBaseTestApplication() { get("/beers") }

                // THEN
                if(response != null) {
                    response.status shouldBe HttpStatusCode.OK
                }
            }
        }
    }
})

fun withBaseTestApplication(request: suspend HttpClient.() -> HttpResponse): HttpResponse? {
    var response: HttpResponse? = null

    testApplication{
        application {
            install(ContentNegotiation) { json() }
            install(Koin) { modules(punkModule) }
            configureRouting()
        }

        response = client.request()
    }

    return response
}