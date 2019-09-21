package xyz.anilkan.kotlin

import com.apurebase.kgraphql.KGraphQL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime
import xyz.anilkan.kotlin.model.MovementType
import xyz.anilkan.kotlin.repository.FirmRepository
import xyz.anilkan.kotlin.repository.SafeRepository
import xyz.anilkan.kotlin.service.AccountingService
import xyz.anilkan.kotlin.service.addExpense

val schema = KGraphQL.schema {
    configure {
        useDefaultPrettyPrinter = true
        objectMapper = jacksonObjectMapper()
    }

    stringScalar<DateTime> {
        serialize = { date -> date.toString() }
        deserialize = { dateString -> DateTime(dateString) }
    }

    enum<MovementType> {
        description = "Type of movement"
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

    // Expense
    mutation("addExpense") {
        resolver { from: Int ->
            AccountingService.addExpense(from)
        }
    }
}