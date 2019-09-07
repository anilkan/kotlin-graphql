package xyz.anilkan.kotlin

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import xyz.anilkan.kotlin.repository.Safes

fun <T> transactionEnviroment(closure: () -> T): T {
    return transaction { closure() }
}

fun createTables() {
    transactionEnviroment {
        SchemaUtils.create(Safes)

        Safes.insert {
            it[code] = "K1"
            it[name] = "Kasa 1"
        }
    }
}

fun connectDatabase() {
    Database.connect(hikari())
}

private fun hikari(): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = "jdbc:postgresql://localhost:5432/test"
    config.username = "postgres"
    config.password = "1234"
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}