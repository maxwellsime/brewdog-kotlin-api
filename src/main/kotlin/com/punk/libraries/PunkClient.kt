package com.punk.libraries

import com.punk.models.Beer
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class PunkClient {
    private lateinit var httpClient: HttpClient

    // Return list of beers from page 1.
    suspend fun getAllBeers(): List<Beer> =
        httpClient.get("https://api.punkapi.com/v2/beers").body()

    suspend fun getAllBeers(page: String?): List<Beer> =
        try {
            httpClient.get("https://api.punkapi.com/v2/beers?page=$page").body()
        } catch(e: Exception) {
            throw e
        }

    suspend fun getBeerByID(id: String?): List<Beer> =
        try {
            httpClient.get("https://api.punkapi.com/v2/beers/$id").body()
        } catch(e: Exception) {
            throw e
        }

    suspend fun getBeerByName(name: String?): List<Beer> =
        try {
            httpClient.get("https://api.punkapi.com/v2/beers?beer_name=$name").body()
        } catch(e: Exception) {
            throw e
        }

    operator fun PunkClient.invoke() {
        httpClient = HttpClient{
            install(ContentNegotiation){
                json(Json {
                    ignoreUnknownKeys = true
                    expectSuccess = false
                })
            }
        }
    }
}


