package xyz.anilkan.kotlin

import io.ktor.application.Application


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.main(testing: Boolean = false) {
    createTables()
}

