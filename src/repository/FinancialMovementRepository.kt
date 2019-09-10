package xyz.anilkan.kotlin.repository

import org.jetbrains.exposed.sql.*
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

fun FinancialMovements.toDataObj(row: ResultRow) = FinancialMovement(
    row[id].toInt(),
    row[datetime].toDateTime(),
    SafeRepository.getElement(row[from].toInt()),
    FirmRepository.getElement(row[to].toInt())
)

object FinancialMovementItems : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val master: Column<Int> = integer("master_id") references FinancialMovements.id
}

fun FinancialMovementItems.toDataObj(row: ResultRow) = FinancialMovementItem(
    row[id].toInt(),
    FinancialMovementRepository.getElement(row[master].toInt())
)

object FinancialMovementRepository : Repository<FinancialMovement> {
    override fun add(element: FinancialMovement): Int =
        transactionEnviroment {
            FinancialMovements.insert {
                it[datetime] = DateTime(element.datetime)
                it[from] = element.from.id
                it[to] = element.to.id
            } get FinancialMovements.id
        }

    override fun getElement(indexer: Int): FinancialMovement =
        transactionEnviroment {
            FinancialMovements
                .select { FinancialMovements.id eq indexer }
                .map { x -> FinancialMovements.toDataObj(x) }
                .first()
        }
}

object FinancialMovementItemRepository : Repository<FinancialMovementItem> {
    override fun add(element: FinancialMovementItem): Int = 0

    override fun getElement(indexer: Int): FinancialMovementItem =
        transactionEnviroment {
            FinancialMovementItems
                .select { FinancialMovementItems.id eq indexer }
                .map { x -> FinancialMovementItems.toDataObj(x) }
                .first()
        }
}