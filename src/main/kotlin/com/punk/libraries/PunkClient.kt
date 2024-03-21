package com.punk.libraries

import com.punk.models.Beer
import io.ktor.client.*
import io.ktor.client.call.*
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
        try {
            httpClient.get("${API_URL}beers?page=$page").body()
        } catch(e: Exception) {
            throw e
        }

    suspend fun getBeerByID(id: Int?): List<Beer> =
        try {
            httpClient.get("${API_URL}beers/$id").body()
        } catch(e: Exception) {
            throw e
        }

    suspend fun getBeersByName(name: String?): List<Beer> =
        try {
            httpClient.get("${API_URL}beers?beer_name=$name").body()
        } catch(e: Exception) {
            throw e
        }

    companion object {
        operator fun invoke(
            apiUrl: String
        ): PunkClient {
            val httpClient = HttpClient{
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
