package xyz.anilkan.kotlin.repository

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import xyz.anilkan.kotlin.model.Safe
import xyz.anilkan.kotlin.transactionEnviroment

object Safes : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val code: Column<String> = varchar("code", 24)
    val name: Column<String> = varchar("name", 255)
}

object SafeRepository : Repository<Safe> {
    override fun getElement(indexer: Int): Safe =
        transactionEnviroment {
            Safes
                .select { Safes.id eq indexer }
                .map { x -> Safe(x[Safes.id].toInt(), x[Safes.code].toString(), x[Safes.name].toString()) }
                .first()
        }
}