package xyz.anilkan.kotlin.service

import xyz.anilkan.kotlin.model.Expense
import xyz.anilkan.kotlin.model.MovementItem
import xyz.anilkan.kotlin.model.Safe
import xyz.anilkan.kotlin.repository.MovementItemRepository
import xyz.anilkan.kotlin.repository.MovementRepository
import xyz.anilkan.kotlin.repository.SafeRepository

object AccountingService

fun AccountingService.addExpense(from: Int, items: List<MovementItem> = emptyList()): Expense {
    val safe: Safe = SafeRepository.getElement(from)

    val expenseId = MovementRepository.add(Expense(0, safe.id))

    items.forEach {
        val item = MovementItem(0, expenseId, it.item, it.amount, it.itemPrice, it.itemTotalPrice)
        MovementItemRepository.add(item)
    }

    return MovementRepository.getElement(expenseId) as Expense
}