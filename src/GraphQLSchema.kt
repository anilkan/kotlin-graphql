package xyz.anilkan.kotlin

import com.apurebase.kgraphql.KGraphQL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime
import xyz.anilkan.kotlin.model.Movement
import xyz.anilkan.kotlin.model.MovementItem
import xyz.anilkan.kotlin.model.MovementType
import xyz.anilkan.kotlin.repository.FirmRepository
import xyz.anilkan.kotlin.repository.MovementRepository
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

    type<Movement> {
        Movement::type.ignore()
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
    query("getExpense") {
        resolver { id: Int ->
            MovementRepository.getElement(id)
        }
    }

    mutation("addExpense") {
        resolver { from: Int, items: Collection<MovementItem> ->
            AccountingService.addExpense(from, items.toList())
        }.withArgs {
            arg<List<MovementItem>> { name = "items"; defaultValue = emptyList() }
        }
    }
}