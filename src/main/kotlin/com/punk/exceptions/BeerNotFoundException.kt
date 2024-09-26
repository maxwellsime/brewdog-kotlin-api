package com.punk.exceptions

import io.ktor.server.plugins.*
import kotlinx.serialization.Serializable

@Serializable
data class BeerNotFoundException(
    val beerId: Int,
    override val message: String = "Beer not found $beerId",
): Exception()
