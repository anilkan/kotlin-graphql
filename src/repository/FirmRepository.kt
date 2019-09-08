package xyz.anilkan.kotlin.repository

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import xyz.anilkan.kotlin.model.Firm
import xyz.anilkan.kotlin.transactionEnviroment

object Firms : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val name: Column<String> = varchar("name", 255)
}

object FirmRepository : Repository<Firm> {
    override fun getElement(indexer: Int): Firm =
        transactionEnviroment {
            Firms
                .select { Firms.id eq indexer }
                .map { x -> Firm(x[Firms.id].toInt(), x[Firms.name].toString()) }
                .first()
        }
}