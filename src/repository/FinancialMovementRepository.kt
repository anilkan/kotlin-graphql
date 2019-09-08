package xyz.anilkan.kotlin.repository

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import xyz.anilkan.kotlin.model.FinancialMovement
import xyz.anilkan.kotlin.model.FinancialMovementItem
import xyz.anilkan.kotlin.transactionEnviroment

object FinancialMovements : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val datetime: Column<DateTime> = datetime("datetime")
    val from: Column<Int> = integer("from_id") references Safes.id
    val to: Column<Int> = integer("to_id") references Firms.id
}

object FinancialMovementItems : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val master: Column<Int> = integer("master_id") references FinancialMovements.id
}

object FinancialMovementRepository : Repository<FinancialMovement> {
    override fun getElement(indexer: Int): FinancialMovement =
        transactionEnviroment {
            FinancialMovements
                .select { FinancialMovements.id eq indexer }
                .map { x ->
                    FinancialMovement(
                        x[FinancialMovements.id].toInt(),
                        x[FinancialMovements.datetime].toDateTime(),
                        SafeRepository.getElement(x[FinancialMovements.from].toInt()),
                        FirmRepository.getElement(x[FinancialMovements.to].toInt())
                    )
                }
                .first()
        }

}

object FinancialMovementItemRepository : Repository<FinancialMovementItem> {
    override fun getElement(indexer: Int): FinancialMovementItem =
        transactionEnviroment {
            FinancialMovementItems
                .select { FinancialMovementItems.id eq indexer }
                .map { x ->
                    FinancialMovementItem(
                        x[FinancialMovementItems.id].toInt(),
                        FinancialMovementRepository.getElement(x[FinancialMovementItems.master].toInt())
                    )
                }
                .first()
        }
}