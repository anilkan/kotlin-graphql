package xyz.anilkan.kotlin

import com.apurebase.kgraphql.KGraphQL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime
import xyz.anilkan.kotlin.repository.FinancialMovementItemRepository
import xyz.anilkan.kotlin.repository.FinancialMovementRepository
import xyz.anilkan.kotlin.repository.FirmRepository
import xyz.anilkan.kotlin.repository.SafeRepository

val schema = KGraphQL.schema {
    configure {
        useDefaultPrettyPrinter = true
        objectMapper = jacksonObjectMapper()
    }

    stringScalar<DateTime> {
        serialize = { date -> date.toString() }
        deserialize = { dateString -> DateTime(dateString) }
    }

    query("safe") {
        resolver { id: Int ->
            SafeRepository.getElement(id)
        }
    }

    query("firm") {
        resolver { id: Int ->
            FirmRepository.getElement(id)
        }
    }

    query("fmovement") {
        resolver { id: Int ->
            FinancialMovementRepository.getElement(id)
        }
    }

    query("fmovementitem") {
        resolver { id: Int ->
            FinancialMovementItemRepository.getElement(id)
        }
    }
}