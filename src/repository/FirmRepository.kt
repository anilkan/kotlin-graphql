package xyz.anilkan.kotlin.repository

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import xyz.anilkan.kotlin.model.Firm
import xyz.anilkan.kotlin.transactionEnviroment

object Firms : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val name: Column<String> = varchar("name", 255)
}

fun Firms.toDataObj(row: ResultRow) = Firm(row[id].toInt(), row[name].toString())

object FirmRepository : Repository<Firm> {
    override fun add(element: Firm): Int = 0

    override fun getElement(indexer: Int): Firm =
        transactionEnviroment {
            Firms
                .select { Firms.id eq indexer }
                .map { x -> Firms.toDataObj(x) }
                .first()
        }
}