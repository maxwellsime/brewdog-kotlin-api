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

// Return list of beers from specific page, using param page.
suspend fun getAllBeers(page: String?): List<Beer>{
    val client = createClient()
    var response: List<Beer> = emptyList()
    try {
        response = client.get("https://api.punkapi.com/v2/beers?page=$page").body()
    } finally{
        client.close()
        return response
    }
}

// Return beer from specific id, using param id.
suspend fun getBeerByID(id: String?): List<Beer>{
    val client = createClient()
    var response: List<Beer> = emptyList()
    try{
        response = client.get("https://api.punkapi.com/v2/beers/$id").body()
    } finally{
        client.close()
        return response
    }
}

// Return beer with specific name, using param name.
suspend fun getBeerByName(name: String?): List<Beer>{
    val client = createClient()
    var response: List<Beer> = emptyList()
    try {
        response = client.get("https://api.punkapi.com/v2/beers?beer_name=$name").body()
    } finally{
        client.close()
        return response
    }
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
