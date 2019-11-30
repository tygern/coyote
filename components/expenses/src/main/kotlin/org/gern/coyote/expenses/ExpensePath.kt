package org.gern.coyote.expenses

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@KtorExperimentalLocationsAPI
@Location("/expenses")
data class ExpensePath(val currency: String? = null, val amount: Double? = null, val instant: Long? = null)

@KtorExperimentalLocationsAPI
fun Route.expenses(repo: ExpenseRepository) {
    get<ExpensePath> {
        call.respond(repo.list().map(::ExpenseInfo))
    }

    post<ExpensePath> {
        val body = call.receive<ExpensePath>()

        val expense = repo.create(
            amount = BigDecimal.valueOf(body.amount!!),
            currency = Currency.getInstance(body.currency!!),
            instant = Instant.ofEpochSecond(body.instant!!)
        )

        call.respond(HttpStatusCode.Created, ExpenseInfo(expense))
    }
}
