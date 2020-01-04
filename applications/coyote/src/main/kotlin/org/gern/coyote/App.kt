package org.gern.coyote

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import org.gern.coyote.expenses.ExpenseRepository
import org.gern.coyote.expenses.ExpenseService
import org.gern.coyote.expenses.expenses

@KtorExperimentalLocationsAPI
fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val jdbcUrl = requireNotNull(System.getenv("JDBC_URL"), {"Error finding JDBC_URL"})

    val db = DatabaseConfiguration(jdbcUrl).db
    val expenseRepository = ExpenseRepository(db)
    val expenseService = ExpenseService(expenseRepository)

    install(Routing) {
        index()
        expenses(expenseService)
    }
}

@KtorExperimentalLocationsAPI
fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080

    embeddedServer(
            factory = Jetty,
            port = port,
            watchPaths = listOf("coyote"),
            module = Application::module
    ).start()
}
