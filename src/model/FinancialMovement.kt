package xyz.anilkan.kotlin.model

import org.joda.time.DateTime
import xyz.anilkan.kotlin.Expense

data class FinancialMovement(
    val id: Int,
    val datetime: DateTime,
    val from: Safe,
    val to: Firm
)

data class FinancialMovementItem(
    val id: Int,
    val master: FinancialMovement
)

fun aaa() {
    val one = Expense(0)
}