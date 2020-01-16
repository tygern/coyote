package test.gern.coyote.expenses

import io.ktor.http.URLBuilder
import org.gern.coyote.expenses.ExpenseRecord
import org.gern.coyote.expenses.ExpenseRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger
import java.nio.ByteBuffer
import java.time.Instant
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExpenseRepositoryTest {
    private val jdbcUrl = requireNotNull(System.getenv("JDBC_URL"), { "Error finding JDBC_URL" })
    private val jdbcUser = requireNotNull(URLBuilder(jdbcUrl).build().parameters["user"], { "Error finding jdbc user" })

    private val db by lazy {
        Database.connect(
            url = jdbcUrl,
            user = jdbcUser,
            driver = "org.mariadb.jdbc.Driver"
        )
    }
    private val repo = ExpenseRepository(db)

    @BeforeTest
    fun setUp() {
        transaction(db) {
            exec("delete from expenses")
        }
    }

    @Test
    fun testCreateSavesToDb() {
        val record = repo.create(
            amount = BigInteger.valueOf(3456L),
            currencyCode = "EUR",
            instant = Instant.ofEpochSecond(1575132999L)
        )

        val result = transaction(db) {
            val dbEntries = mutableListOf<Map<String, Any>>()

            exec("select * from expenses") { rs ->
                while (rs.next()) {
                    dbEntries.add(mapOf(
                        "id" to ByteBuffer.wrap(rs.getBinaryStream("id").readBytes()).let { UUID(it.long, it.long).toString() },
                        "amount" to rs.getLong("amount"),
                        "currency_code" to rs.getString("currency_code"),
                        "instant" to rs.getLong("instant")
                    ))
                }
            }

            dbEntries
        }

        assertEquals(1, result.size)

        assertEquals(record.id.toString(), result[0]["id"])
        assertEquals(3456L, result[0]["amount"])
        assertEquals("EUR", result[0]["currency_code"])
        assertEquals(1575132999L, result[0]["instant"])
    }

    @Test
    fun testCreateReturn() {
        val record = repo.create(
            amount = BigInteger.valueOf(3456L),
            currencyCode = "EUR",
            instant = Instant.ofEpochSecond(1575132999L)
        )

        assertEquals(
            ExpenseRecord(
                id = record.id,
                amount = BigInteger.valueOf(3456L),
                currencyCode = "EUR",
                instant = Instant.ofEpochSecond(1575132999L)
            ),
            record
        )
    }

    @Test
    fun testFind() {
        val record = repo.create(
            amount = BigInteger.valueOf(3456L),
            currencyCode = "EUR",
            instant = Instant.ofEpochSecond(1575132999L)
        )

        val result = repo.find(record.id)

        assertEquals(
            ExpenseRecord(
                id = record.id,
                amount = BigInteger.valueOf(3456L),
                currencyCode = "EUR",
                instant = Instant.ofEpochSecond(1575132999L)
            ),
            result
        )
    }

    @Test
    fun testFindNotThere() {
        assertNull(repo.find(UUID.randomUUID()))
    }

    @Test
    fun testList() {
        val record = repo.create(
            amount = BigInteger.valueOf(3456L),
            currencyCode = "EUR",
            instant = Instant.ofEpochSecond(1575132999L)
        )

        val result = repo.list()

        assertEquals(
            setOf(
                ExpenseRecord(
                    id = record.id,
                    amount = BigInteger.valueOf(3456L),
                    currencyCode = "EUR",
                    instant = Instant.ofEpochSecond(1575132999L)
                )
            ),
            result
        )
    }

    @Test
    fun testDelete() {
        val recordToDelete = repo.create(
            amount = BigInteger.valueOf(3456L),
            currencyCode = "EUR",
            instant = Instant.ofEpochSecond(1575132999L)
        )

        val recordToKeep = repo.create(
            amount = BigInteger.valueOf(3456L),
            currencyCode = "EUR",
            instant = Instant.ofEpochSecond(1575132999L)
        )

        repo.delete(recordToDelete.id)

        assertEquals(listOf(recordToKeep.id), repo.list().map(ExpenseRecord::id))
    }
}
