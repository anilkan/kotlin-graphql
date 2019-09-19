package xyz.anilkan.kotlin.repository

import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import org.postgresql.util.PGobject
import xyz.anilkan.kotlin.Expense
import xyz.anilkan.kotlin.Income
import xyz.anilkan.kotlin.Movement
import xyz.anilkan.kotlin.MovementType
import xyz.anilkan.kotlin.model.FinancialMovement
import xyz.anilkan.kotlin.model.FinancialMovementItem
import xyz.anilkan.kotlin.util.transactionEnviroment

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

object Movements : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val type = customEnumeration(
        "type",
        "MovementType",
        { value -> MovementType.valueOf(value as String) },
        { PGEnum("MovementType", it) })
}

fun Movements.toDataObj(row: ResultRow): Movement {
    return when (row[type]) {
        MovementType.EXPENSE -> Expense(row[id])
        MovementType.INCOME -> Income(row[id])
    }
}

object MovementRepository : Repository<Movement> {
    override fun add(element: Movement): Int =
        transactionEnviroment {
            Movements.insert {
                it[type] = element.type
            } get Movements.id
        }

    override fun getElement(indexer: Int): Movement =
        transactionEnviroment {
            Movements
                .select { Movements.id eq indexer }
                .map { x -> Movements.toDataObj(x) }
                .first()
        }
}

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