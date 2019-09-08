package xyz.anilkan.kotlin

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson


@Suppress("UNUSED") // Referenced in application.conf
fun Application.main() {
    install(CallLogging)
    install(DefaultHeaders)
    install(ContentNegotiation) {
        jackson {
            //disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
    connectDatabase()
    createTables()
}

