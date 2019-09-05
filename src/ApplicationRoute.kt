package xyz.anilkan.kotlin

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.routes() {
    routing {
        get("/hello") {
            call.respondText("Hello World")
        }
    }
}