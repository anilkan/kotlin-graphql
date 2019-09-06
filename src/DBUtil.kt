package xyz.anilkan.kotlin

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.anilkan.kotlin.repository.Safes

fun <T> transactionEnviroment(closure: () -> T): T {
    Database.connect(
        "jdbc:postgresql://localhost:5432/test", driver = "org.postgresql.Driver",
        user = "postgres", password = "1234"
    )
    return transaction { closure() }
}

fun createTables() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/test", driver = "org.postgresql.Driver",
        user = "postgres", password = "1234"
    )
    transaction {
        SchemaUtils.create(Safes)

        Safes.insert {
            it[code] = "K1"
            it[name] = "Kasa 1"
        }
    }
}