package test.gern.coyote

import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.handleRequest
import kotlin.test.Test
import kotlin.test.assertEquals

@KtorExperimentalLocationsAPI
class AppTest {
    @Test
    fun testEmptyHome() = testApp {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(200, response.status()?.value)

            val body = mapper.readTree(response.content!!)
            assertEquals("coyote", body["application"].asText())
        }
    }
}
