package xyz.anilkan.kotlin

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import xyz.anilkan.kotlin.graphql.schema

@Suppress("unused") // Referenced in application.conf
fun Application.route() {
    routing {
        get("/hello") {
            call.respondText("Hello World")
        }

        post("/graphql") {
            val request = call.receiveText()

            val res = schema.runCatching {
                execute(request)
            }

            val response = res.fold(onSuccess = { it }, onFailure = {
                """
                {
                    "errors": ${it.message}
                }
            """.trimIndent()
            })

            call.respond(response)
        }
    }
}