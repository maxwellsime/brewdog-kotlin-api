package com.punk.exceptions;

import io.ktor.server.plugins.*

data class NoBeersFoundException(
    val function: String,
    override val message: String = "No beers found in $function",
): Exception() {

    fun toNotFoundException() = NotFoundException(message)
}
