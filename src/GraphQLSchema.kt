package xyz.anilkan.kotlin

import com.apurebase.kgraphql.KGraphQL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime
import xyz.anilkan.kotlin.model.FinancialMovement
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

    // Safe
    query("safe") {
        resolver { id: Int ->
            SafeRepository.getElement(id)
        }
    }

    // Firm
    query("firm") {
        resolver { id: Int ->
            FirmRepository.getElement(id)
        }
    }

    // Financial Movement
    query("fmovement") {
        resolver { id: Int ->
            FinancialMovementRepository.getElement(id)
        }
    }

    mutation("createFinancialMovement") {
        resolver { datetime: DateTime, from: Int, to: Int ->
            FinancialMovementRepository.getElement(
                FinancialMovementRepository.add(
                    FinancialMovement(
                        0,
                        datetime,
                        SafeRepository.getElement(from),
                        FirmRepository.getElement(to)
                    )
                )
            )
        }.withArgs { }
    }

    query("fmovementitem") {
        resolver { id: Int ->
            FinancialMovementItemRepository.getElement(id)
        }
    }
}