package xyz.anilkan.kotlin

import com.apurebase.kgraphql.KGraphQL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import xyz.anilkan.kotlin.repository.SafeRepository

val schema = KGraphQL.schema {
    configure {
        useDefaultPrettyPrinter = true
        objectMapper = jacksonObjectMapper()
    }

    query("safe") {
        resolver { id: Int ->
            SafeRepository.getElement(id)
        }
    }
}