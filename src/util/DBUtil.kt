package xyz.anilkan.kotlin.util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PGobject
import xyz.anilkan.kotlin.repository.Firms
import xyz.anilkan.kotlin.repository.MovementItems
import xyz.anilkan.kotlin.repository.Movements
import xyz.anilkan.kotlin.repository.Safes

fun <T> transactionEnvironment(closure: () -> T): T {
    return transaction { closure() }
}

// TODO: FlyWay ile db versiyonlama yapılmalı
fun createTables() {
    transactionEnvironment {
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

        SchemaUtils.create(Movements)

        SchemaUtils.create(MovementItems)
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

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}