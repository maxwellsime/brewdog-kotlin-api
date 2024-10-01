package com.punk.exceptions

import kotlinx.serialization.Serializable

@Serializable
data class NoBeersFoundException(
    val function: String,
    override val message: String = "No beers found in $function",
): Exception()
