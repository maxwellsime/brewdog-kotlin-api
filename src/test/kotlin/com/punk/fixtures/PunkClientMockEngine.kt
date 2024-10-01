package com.punk.fixtures

import com.punk.fixtures.BeerTestFixtures.BEER_NAME
import com.punk.fixtures.BeerTestFixtures.ID
import com.punk.fixtures.BeerTestFixtures.genericBeerListJson
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*

object PunkClientMockEngine {

    /*
        In reality this should be a class calling functions for each outwards route.
        As this currently is, scaling the available tested routes would make this a horror show.
    */

    const val API_URL = "https://mock.api.com/"
    const val PAGE = 2
    const val INVALID_PAGE = 100
    const val INVALID_ID = 999

    val punkClientMockEngine = MockEngine {
        request -> when(request.url.encodedPathAndQuery) {
            "/beers" ->
                respond(
                    content = ByteReadChannel(genericBeerListJson),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            "/beers?page=$PAGE" ->
                respond(
                    content = ByteReadChannel(genericBeerListJson),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            "/beers?page=$INVALID_PAGE" ->
                respond(
                    content = ByteReadChannel("""[]"""),
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            "/beers/$ID" ->
                respond(
                    content = ByteReadChannel(genericBeerListJson),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            "/beers/$INVALID_ID" ->
                respond(
                    content = ByteReadChannel("""[]"""),
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            "/beers?beer_name=$BEER_NAME" ->
                respond(
                    content = ByteReadChannel(genericBeerListJson),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            else ->
                respond(
                    content = ByteReadChannel.Empty,
                    status = HttpStatusCode.InternalServerError,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
        }
    }
}
