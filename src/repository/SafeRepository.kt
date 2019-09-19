package xyz.anilkan.kotlin.repository

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import xyz.anilkan.kotlin.model.Safe
import xyz.anilkan.kotlin.util.transactionEnviroment

object Safes : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val code: Column<String> = varchar("code", 24)
    val name: Column<String> = varchar("name", 255)
    val balance: Column<Double> = double("balance").default(0.0)
}

fun Safes.toDataObj(row: ResultRow) = Safe(
    row[id].toInt(),
    row[code].toString(),
    row[name].toString(),
    row[balance].toDouble()
)

object SafeRepository : Repository<Safe> {
    override fun add(element: Safe): Int = 0

    override fun getElement(indexer: Int): Safe =
        transactionEnviroment {
            Safes
                .select { Safes.id eq indexer }
                .map { x -> Safes.toDataObj(x) }
                .first()
        }
}