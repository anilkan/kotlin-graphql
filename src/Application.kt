package xyz.anilkan.kotlin

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import xyz.anilkan.kotlin.model.Expense
import xyz.anilkan.kotlin.model.Income
import xyz.anilkan.kotlin.model.MovementItem
import xyz.anilkan.kotlin.repository.FirmRepository
import xyz.anilkan.kotlin.repository.MovementItemRepository
import xyz.anilkan.kotlin.repository.MovementRepository
import xyz.anilkan.kotlin.repository.SafeRepository
import xyz.anilkan.kotlin.util.connectDatabase
import xyz.anilkan.kotlin.util.createTables

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

    val expense = Expense(0, SafeRepository.getElement(18))
    val income = Income(0, FirmRepository.getElement(20))

    val expenseId = MovementRepository.add(expense)
    val incomeId = MovementRepository.add(income)

    println(MovementRepository.getElement(expenseId))
    println(MovementRepository.getElement(incomeId))

    val expenseItem = MovementItem(0, MovementRepository.getElement(12), "Muz", 1.345f, 10f, 13.45f)
    val expenseItemId = MovementItemRepository.add(expenseItem)

    println(MovementItemRepository.getElement(expenseItemId))

    println(MovementItemRepository.getElementsByMovementId(12))

    println(MovementRepository.getElement(12))

    println()
    println(MovementRepository.getElement(12).items)
}
