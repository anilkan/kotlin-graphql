package xyz.anilkan.kotlin.graphql

import com.apurebase.kgraphql.KGraphQL
import xyz.anilkan.kotlin.repository.SafeRepository

val schema = KGraphQL.schema {
    configure {
        useDefaultPrettyPrinter = true
    }

    query("getSafe") {
        resolver { id: Int ->
            SafeRepository.getElement(id)
        }
    }
}