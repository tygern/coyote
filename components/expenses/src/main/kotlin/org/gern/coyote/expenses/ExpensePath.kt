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
data class ExpensePath(val currency: String? = null, val amount: Double? = null, val instant: Long? = null) {
    val valid = amount != null
        && instant != null
        && validCurrencyCode()

    private fun validCurrencyCode() = try {
        Currency.getInstance(currency)
        true
    } catch (e: NullPointerException) {
        false
    } catch (e: IllegalArgumentException) {
        false
    }
}

@KtorExperimentalLocationsAPI
fun Route.expenses(service: ExpenseService) {
    get<ExpensePath> {
        call.respond(service.list().map(::ExpenseInfo))
    }

    post<ExpensePath> {
        val requestBody = call.receive<ExpensePath>()

        if (requestBody.valid) {
            val expense = service.create(
                amount = BigDecimal.valueOf(requestBody.amount!!),
                currency = Currency.getInstance(requestBody.currency!!),
                instant = Instant.ofEpochSecond(requestBody.instant!!)
            )

            call.respond(HttpStatusCode.Created, ExpenseInfo(expense))
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
