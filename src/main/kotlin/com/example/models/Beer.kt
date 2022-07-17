package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Beer (
    val id: Int,
    val name: String,
    val description: String
)

val beerStorage = mutableListOf<Beer>()