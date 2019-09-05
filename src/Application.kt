package xyz.anilkan.kotlin

import com.apurebase.kgraphql.KGraphQL
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Kasalar : Table() {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val kod: Column<String> = varchar("kod", 24)
    val isim: Column<String> = varchar("isim", 255)
}

data class Kasa(
    val id: Int,
    val kod: String,
    val isim: String
)

//fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.main(testing: Boolean = false) {

    val nb = Kasa(1, "K1", "Kasa 1")
    val nk = Kasa(2, "K2", "Kasa 2")

    val db = Database.connect(
        "jdbc:postgresql://localhost:5432/test", driver = "org.postgresql.Driver",
        user = "postgres", password = "1234"
    )

    fun toKasa(row: ResultRow): Kasa = Kasa(
        id = row[Kasalar.id],
        kod = row[Kasalar.kod],
        isim = row[Kasalar.isim]
    )

    fun getKasaById(id: Int?): Kasa =
        transaction {
            if (id != null) {
                Kasalar.select {
                    (Kasalar.id eq id)
                }.map { toKasa(it) }
                    .single()
            } else {
                error("ID gelmemişki kasayı bulalım!")
            }
        }


    transaction {
        SchemaUtils.create(Kasalar)

        Kasalar.insert {
            it[kod] = "K1"
            it[isim] = "Kasa 1"
        }

        Kasalar.insert {
            it[kod] = "K2"
            it[isim] = "Kasa 2"
        }
    }

    val schema = KGraphQL.schema {
        configure {
            useDefaultPrettyPrinter = true
        }

        query("kasa") {
            resolver { id: Int? ->
                getKasaById(id)
            }.withArgs {
                arg<Int> { name = "id"; defaultValue = 1; }
            }
        }
    }

    val query = """
            query KasaDetaylari(${"$"}_id: Int) {
                kasa(id: ${"$"}_id) {
                    id
                    kod
                    isim
                }
            }
    """.trimIndent()


    routing {
        get("/{id?}") {
            val id = call.parameters["id"]
            val variables = """
                {
                    "_id": $id
                }
            """.trimIndent()
            call.respondText(schema.execute(query, variables))
        }
    }
}

