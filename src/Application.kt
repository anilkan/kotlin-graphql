package xyz.anilkan.kotlin

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import xyz.anilkan.kotlin.repository.MovementRepository
import xyz.anilkan.kotlin.util.connectDatabase
import xyz.anilkan.kotlin.util.createTables

enum class MovementType {
    EXPENSE, INCOME
}

sealed class Movement {
    abstract val id: Int
    abstract val type: MovementType
}

data class Expense(override val id: Int) : Movement() {
    override val type: MovementType = MovementType.EXPENSE
}

data class Income(override val id: Int) : Movement() {
    override val type: MovementType = MovementType.INCOME
}

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

    val expense = Expense(0)
    val income = Income(0)

    MovementRepository.add(expense)
    MovementRepository.add(income)

    println(MovementRepository.getElement(1))
    println(MovementRepository.getElement(2))
}

