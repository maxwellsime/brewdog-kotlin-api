package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
    }.start(wait = true)
}
