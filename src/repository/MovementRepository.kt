package xyz.anilkan.kotlin.repository

import org.jetbrains.exposed.sql.*
import xyz.anilkan.kotlin.model.*
import xyz.anilkan.kotlin.util.PGEnum
import xyz.anilkan.kotlin.util.transactionEnvironment

object Movements : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val type = customEnumeration(
        "type",
        "MovementType",
        { value -> MovementType.valueOf(value as String) },
        { PGEnum("MovementType", it) })
    val from: Column<Int> = integer("from")
}

fun Movements.toDataObj(row: ResultRow): Movement {
    return when (row[type]) {
        MovementType.EXPENSE -> Expense(row[id], row[from])
        MovementType.INCOME -> Income(row[id], row[from])
    }
}

object MovementRepository : Repository<Movement> {
    override fun add(element: Movement): Int =
        transactionEnvironment {
            Movements.insert {
                it[type] = element.type
                it[from] = element.from
            } get Movements.id
        }

    override fun getElement(indexer: Int): Movement =
        transactionEnvironment {
            Movements
                .select { Movements.id eq indexer }
                .map { x -> Movements.toDataObj(x) }
                .first()
        }
}

object MovementItems : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val movement: Column<Int> = integer("movement_id") references Movements.id
    val item: Column<String> = varchar("item", 250)
    val amount: Column<Float> = float("amount")
    val itemPrice: Column<Float> = float("item_price")
    val itemTotalPrice: Column<Float> = float("item_total_price")
}

fun MovementItems.toDataObj(row: ResultRow): MovementItem = MovementItem(
    row[id],
    row[movement],
    row[item],
    row[amount],
    row[itemPrice],
    row[itemTotalPrice]
)

object MovementItemRepository : Repository<MovementItem> {
    override fun add(element: MovementItem): Int =
        transactionEnvironment {
            MovementItems.insert {
                it[movement] = element.movement
                it[item] = element.item
                it[amount] = element.amount
                it[itemPrice] = element.itemPrice
                it[itemTotalPrice] = element.itemTotalPrice
            } get MovementItems.id
        }

    override fun getElement(indexer: Int): MovementItem =
        transactionEnvironment {
            MovementItems
                .select { MovementItems.id eq indexer }
                .map { x -> MovementItems.toDataObj(x) }
                .first()
        }

    fun getElementsByMovementId(movementId: Int): List<MovementItem> =
        transactionEnvironment {
            MovementItems
                .select { MovementItems.movement eq movementId }
                .map { x -> MovementItems.toDataObj(x) }
        }
}