package com.punk.libraries

import com.punk.models.Beer
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class PunkClient(
    private val httpClient: HttpClient
) {

    suspend fun getAllBeers(): List<Beer> =
        httpClient.get("${API_URL}beers").body()

    suspend fun getAllBeers(page: Int?): List<Beer> =
        httpClient.get("${API_URL}beers?page=$page").body()

    suspend fun getBeerByID(id: Int?): List<Beer> =
        httpClient.get("${API_URL}beers/$id").body()

    suspend fun getBeersByName(name: String?): List<Beer> =
        httpClient.get("${API_URL}beers?beer_name=$name").body()

    companion object {
        operator fun invoke(
            apiUrl: String,
            engine: HttpClientEngine = CIO.create()
        ): PunkClient {
            val httpClient = HttpClient(engine) {
                install(ContentNegotiation){
                    json(Json {
                        ignoreUnknownKeys = true
                        expectSuccess = false
                    })
                }
            }

            API_URL = apiUrl

            // auth here
            return PunkClient(httpClient)
        }

        private lateinit var API_URL: String
    }
}
