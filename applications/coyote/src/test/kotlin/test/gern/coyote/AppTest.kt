package test.gern.coyote

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.HttpMethod
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.gern.coyote.module
import org.junit.Test
import kotlin.test.assertEquals

@KtorExperimentalLocationsAPI
class AppTest {
    @Test
    fun testEmptyHome() = testApp {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(200, response.status()?.value)

            val body = jacksonObjectMapper().readTree(response.content!!)
            assertEquals("coyote", body["application"].asText())
        }
    }

    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module() }) { callback() }
    }
}
