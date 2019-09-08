package xyz.anilkan.kotlin.model

import org.joda.time.DateTime

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