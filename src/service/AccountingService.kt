package xyz.anilkan.kotlin.service

import xyz.anilkan.kotlin.model.Expense
import xyz.anilkan.kotlin.model.Safe
import xyz.anilkan.kotlin.repository.MovementRepository
import xyz.anilkan.kotlin.repository.SafeRepository

object AccountingService

fun AccountingService.addExpense(from: Int): Expense {
    val safe: Safe = SafeRepository.getElement(from)

    val expenseId = MovementRepository.add(Expense(0, safe.id))

    return MovementRepository.getElement(expenseId) as Expense
}