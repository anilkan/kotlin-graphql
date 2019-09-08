package xyz.anilkan.kotlin

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import xyz.anilkan.kotlin.repository.FinancialMovements
import xyz.anilkan.kotlin.repository.Firms
import xyz.anilkan.kotlin.repository.Safes

fun <T> transactionEnviroment(closure: () -> T): T {
    return transaction { closure() }
}

// TODO: FlyWay ile db versioning yapılmalı
fun createTables() {
    transactionEnviroment {
        SchemaUtils.create(Safes)
        val safeId = Safes.insert {
            it[code] = "K1"
            it[name] = "Kasa 1"
            it[balance] = 0.0
        } get Safes.id

        SchemaUtils.create(Firms)
        val firmId = Firms.insert {
            it[name] = "Pehlivanoğlu"
        } get Firms.id

        SchemaUtils.create(FinancialMovements)
        val movementId = FinancialMovements.insert {
            it[datetime] = DateTime.now()
            it[from] = safeId
            it[to] = firmId
        } get FinancialMovements.id
    }
}

// TODO: Uygulama başladıktan sonra otomatik çağırılmalı
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