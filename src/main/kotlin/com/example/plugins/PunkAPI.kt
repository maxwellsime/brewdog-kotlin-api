package com.example.plugins

import com.example.models.Beer
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

// Return list of beers from page 1.
suspend fun getAllBeers(): List<Beer>{
    val client = createClient()
    val response: List<Beer> = client.get("https://api.punkapi.com/v2/beers").body()
    client.close()
    return response
}

// Return beer from specific id, using param id.
suspend fun getBeerByID(id: String?): Beer{
    val client = createClient()
    var response = client.get("https://api.punkapi.com/v2/beers/$id").body()

    client.close()
    return response[0]
}

// Create httpclient for use in each http get call.
fun createClient(): HttpClient{
    val client = HttpClient{
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
                expectSuccess = false
            })
        }
    }
    return client
}