package test.gern.coyote

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@KtorExperimentalLocationsAPI
class ExpensesApiTest {

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
    fun testListExpense() = testApp {
        postRequest("/expenses", expenseData)
                .apply { assertEquals(201, response.status()?.value) }

        handleRequest(HttpMethod.Get, "/expenses").apply {
            assertEquals(HttpStatusCode.OK.value, response.status()?.value)

            val expense = mapper.readTree(response.content!!).asIterable().firstOrNull()

            assertNotNull(expense)

            assertNotNull(expense["id"])
            assertEquals("USD", expense["currency"].asText())
            assertEquals(43.67, expense["amount"].asDouble())
            assertEquals(1575132000L, expense["instant"].asLong())
        }
    }

    private fun TestApplicationEngine.postRequest(location: String, body: Map<String, Any>) =
            handleRequest(HttpMethod.Post, location) {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(mapper.writeValueAsString(body))
            }
}
