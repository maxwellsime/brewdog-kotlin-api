package com.punk.exceptions

import io.ktor.http.*
import io.ktor.server.plugins.*

data class BeerNotFoundException(
    val beerId: Int,
    override val message: String = "Beer not found $beerId",
): Exception() {

    fun toNotFoundException() = NotFoundException(message)
}
