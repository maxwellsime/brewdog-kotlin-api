package com.example.models

import kotlinx.serialization.Serializable

// Created specifically to return JSON as error message.

@Serializable
data class ErrorMessage(
    // Breaking convention for presentation purposes.
    val Error: String
)