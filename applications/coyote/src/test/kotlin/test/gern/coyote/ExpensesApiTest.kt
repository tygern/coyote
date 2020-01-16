package test.gern.coyote

import io.ktor.http.*
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.test.*

@KtorExperimentalLocationsAPI
class ExpensesApiTest {
    private val jdbcUrl = requireNotNull(System.getenv("JDBC_URL"), { "Error finding JDBC_URL" })
    private val jdbcUser = requireNotNull(URLBuilder(jdbcUrl).build().parameters["user"], { "Error finding jdbc user" })

    private val db by lazy {
        Database.connect(
            url = jdbcUrl,
            user = jdbcUser,
            driver = "org.mariadb.jdbc.Driver"
        )
    }

    @BeforeTest
    fun setUp() {
        transaction(db) {
            exec("delete from expenses")
        }
    }

    private val expenseData = mapOf(
        "currency" to "USD",
        "amount" to 43.67,
        "instant" to 1575132000L
    )

    @Test
    fun testNoExpenses() = testApp {
        handleRequest(HttpMethod.Get, "/expenses").apply {
            assertEquals(HttpStatusCode.OK.value, response.status()?.value)
            assertEquals("[ ]", response.content!!)
        }
    }

    @Test
    fun testAddExpense() = testApp {
        postRequest("/expenses", expenseData).apply {
            assertEquals(201, response.status()?.value)

            val body = mapper.readTree(response.content!!)

            assertFalse(body["id"].asText().isNullOrEmpty())
            assertEquals("USD", body["currency"].asText())
            assertEquals(43.67, body["amount"].asDouble())
            assertEquals(1575132000L, body["instant"].asLong())
        }
    }

    @Test
    fun testAddExpenseMissingInfo() = testApp {
        postRequest("/expenses", mapOf(
            "amount" to 43.67
        )).apply {
            assertEquals(400, response.status()?.value)
        }
    }

    @Test
    fun findExpense() = testApp {
        val id = createExpense()

        handleRequest(HttpMethod.Get, "/expenses/${id}").apply {
            assertEquals(HttpStatusCode.OK.value, response.status()?.value)

            val expense = mapper.readTree(response.content!!)

            assertEquals(id, expense["id"].asText())
            assertEquals("USD", expense["currency"].asText())
            assertEquals(43.67, expense["amount"].asDouble())
            assertEquals(1575132000L, expense["instant"].asLong())
        }
    }

    @Test
    fun findExpenseNotFound() = testApp {
        val id = UUID.randomUUID().toString()

        val result = handleRequest(HttpMethod.Get, "/expenses/${id}")

        assertEquals(HttpStatusCode.NotFound.value, result.response.status()?.value)
    }

    @Test
    fun testListExpense() = testApp {
        postRequest("/expenses", expenseData)
            .apply { assertEquals(201, response.status()?.value) }

        handleRequest(HttpMethod.Get, "/expenses").apply {
            assertEquals(HttpStatusCode.OK.value, response.status()?.value)

            val expense = mapper.readTree(response.content!!).asIterable().firstOrNull()

            assertNotNull(expense)

            assertFalse(expense["id"].asText().isNullOrEmpty())
            assertEquals("USD", expense["currency"].asText())
            assertEquals(43.67, expense["amount"].asDouble())
            assertEquals(1575132000L, expense["instant"].asLong())
        }
    }

    private fun TestApplicationEngine.createExpense(): String {
        val createResponse = postRequest("/expenses", expenseData)
            .apply { assertEquals(201, response.status()?.value) }
            .response

        return mapper.readTree(createResponse.content!!)["id"].asText()!!
    }

    private fun TestApplicationEngine.postRequest(location: String, body: Map<String, Any>) =
        handleRequest(HttpMethod.Post, location) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(mapper.writeValueAsString(body))
        }
}
