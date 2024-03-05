package com.punk.models

import kotlinx.serialization.Serializable

@Serializable
data class BeersRequest (
    val name: String? = null,
    val page: Int? = null,
)
