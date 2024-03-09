package com.punk.models

import kotlinx.serialization.Serializable

@Serializable
data class BeersResponse(
    val beers: List<Beer>
)
